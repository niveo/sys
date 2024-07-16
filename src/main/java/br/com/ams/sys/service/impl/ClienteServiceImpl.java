package br.com.ams.sys.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ams.sys.common.Constante;
import br.com.ams.sys.common.RestPage;
import br.com.ams.sys.config.RedisConfig;
import br.com.ams.sys.entity.Cliente;
import br.com.ams.sys.entity.RedeCliente;
import br.com.ams.sys.entity.SegmentoCliente;
import br.com.ams.sys.entity.TabelaPreco;
import br.com.ams.sys.records.BairroDto;
import br.com.ams.sys.records.CidadeDto;
import br.com.ams.sys.records.ClienteDto;
import br.com.ams.sys.records.ClienteListaDto;
import br.com.ams.sys.records.ClienteRegistrarDto;
import br.com.ams.sys.records.EnderecoDto;
import br.com.ams.sys.records.EstadoDto;
import br.com.ams.sys.records.RedeClienteDto;
import br.com.ams.sys.records.SegmentoClienteDto;
import br.com.ams.sys.records.TabelaPrecoDto;
import br.com.ams.sys.repository.ClienteRepository;
import br.com.ams.sys.service.BairroService;
import br.com.ams.sys.service.CacheService;
import br.com.ams.sys.service.CidadeService;
import br.com.ams.sys.service.ClienteService;
import br.com.ams.sys.service.EmpresaService;
import br.com.ams.sys.service.RedeClienteService;
import br.com.ams.sys.service.SegmentoClienteService;
import br.com.ams.sys.service.TabelaPrecoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class ClienteServiceImpl implements ClienteService {
	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private CidadeService cidadeService;

	@Autowired
	private BairroService bairroService;

	@Autowired
	private EmpresaService empresaService;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private CacheService cacheService;

	@Autowired
	private SegmentoClienteService segmentoClienteService;

	@Autowired
	private RedeClienteService redeClienteService;

	@Autowired
	private TabelaPrecoService tabelaPrecoService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Cliente save(Cliente entidade) throws Exception {
		cacheService.clear(RedisConfig.CACHE_EMPRESAS_KEY);
		return clienteRepository.save(entidade);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ClienteDto save(Long codigoEmpresa, ClienteRegistrarDto entidade) throws Exception {
		var cidade = cidadeService.findByCodigo(entidade.endereco().cidade().codigo());
		var bairro = bairroService.findByCodigo(entidade.endereco().bairro().codigo());

		SegmentoCliente segmento = null;
		if (entidade.segmento() != null)
			segmento = segmentoClienteService.findByCodigo(entidade.segmento().codigo());

		RedeCliente rede = null;
		if (entidade.rede() != null)
			rede = redeClienteService.findByCodigo(entidade.rede().codigo());

		TabelaPreco tabela = null;
		if (entidade.tabela() != null)
			tabela = tabelaPrecoService.findByCodigo(entidade.tabela().codigo());

		var empresa = empresaService.findByCodigo(codigoEmpresa);

		Cliente registrar;
		if (entidade.codigo() != null) {
			var emp = clienteRepository.findById(entidade.codigo()).get();
			registrar = entidade.toCliente(emp);
		} else {
			registrar = entidade.toCliente(new Cliente());
		}

		registrar.setEmpresa(empresa);
		registrar.setSegmento(segmento);
		registrar.setTabela(tabela);
		registrar.setRede(rede);
		registrar.getEndereco().setCep(registrar.getEndereco().getCep().replace("-", "").trim());
		registrar.getEndereco().setBairro(bairro);
		registrar.getEndereco().setCidade(cidade);

		registrar = save(registrar);

		return obterCodigo(codigoEmpresa, registrar.getCodigo());
	}

	@Override
	public Cliente findByCodigo(Long codigo) throws Exception {
		return this.clienteRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteByCodigo(Long codigo) {
		cacheService.clear(RedisConfig.CACHE_SEGMENTO_CLIENTES_KEY);
		this.clienteRepository.deleteById(codigo);
	}

	@Override
	@Cacheable(value = RedisConfig.CACHE_EMPRESAS_KEY)
	public Page<ClienteListaDto> obterTodos(Long codigoEmpresa, Integer page, String conditions) throws Exception {
		page--;
		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(ClienteListaDto.class);
		var root = query.from(Cliente.class);

		var select = cb.construct(ClienteListaDto.class, root.get("codigo"), root.get("documento"), root.get("nome"),
				root.get("razaoSocial"), root.get("telefone"), root.get("email"), root.get("inscricaoEstadual"),
				root.get("tipoPessoa"));

		query.select(select);

		var predicates = obterPredicates(codigoEmpresa, cb, root, conditions);
		if (predicates.length > 0)
			query.where(predicates);

		query.orderBy(cb.asc(root.get("codigo")));

		var registros = entityManager.createQuery(query).setFirstResult(page * Constante.PAGINA_REGISTROS)
				.setMaxResults(Constante.PAGINA_REGISTROS).getResultList();

		return new RestPage<ClienteListaDto>(registros, page, Constante.PAGINA_REGISTROS,
				contarRegistros(codigoEmpresa, cb, conditions));
	}

	private Long contarRegistros(Long codigoEmpresa, CriteriaBuilder cb, String conditions) throws Exception {

		// Create Count Query
		var countQuery = cb.createQuery(Long.class);
		var root = countQuery.from(Cliente.class);

		var predicates = obterPredicates(codigoEmpresa, cb, root, conditions);
		if (predicates.length > 0)
			countQuery.where(predicates);

		countQuery.select(cb.count(root));
		return entityManager.createQuery(countQuery).getSingleResult();
	}

	private Predicate[] obterPredicates(Long codigoEmpresa, CriteriaBuilder cb, Root<Cliente> root, String conditions)
			throws Exception {

		JsonNode filtros = null;
		var predicates = new ArrayList<Predicate>();

		if (conditions != null && !conditions.isEmpty()) {
			filtros = new ObjectMapper().readTree(conditions);
			var itFiltros = filtros.fieldNames();
			while (itFiltros.hasNext()) {
				var name = itFiltros.next();
				var node = filtros.get(name);

				if (node.asText().isEmpty())
					continue;

				if ("codigo".equals(name)) {
					var predValue = cb.equal(root.get("codigo"), node.asLong());
					predicates.add(cb.or(predValue));
				}
				if ("nome".equals(name)) {
					var predValue = cb.like(root.get("nome"), "'%" + node.asText() + "%");
					predicates.add(cb.or(predValue));
				}
				if ("razaoSocial".equals(name)) {
					var predValue = cb.like(root.get("razaoSocial"), "%" + node.asText() + "%");
					predicates.add(cb.or(predValue));
				}
				if ("documento".equals(name)) {
					var predValue = cb.like(root.get("documento"), "%" + node.asText() + "%");
					predicates.add(cb.or(predValue));
				}
			}
		}

		if (codigoEmpresa != null) {
			var predValue = cb.equal(root.get("empresa").get("codigo"), codigoEmpresa);
			predicates.add(predValue);
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	public ClienteDto obterCodigo(Long codigoEmpresa, Long codigo) throws Exception {
		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(ClienteDto.class);
		var root = query.from(Cliente.class);

		var segmento = root.join("segmento", JoinType.LEFT);
		var rede = root.join("rede", JoinType.LEFT);
		var tabela = root.join("tabela", JoinType.LEFT);

		var endereco = root.get("endereco");
		var cidade = endereco.get("cidade");
		var estado = cidade.get("estado");
		var bairro = endereco.get("bairro");

		var selectSegmento = cb.construct(SegmentoClienteDto.class, segmento.get("codigo"), segmento.get("descricao"));

		var selectRede = cb.construct(RedeClienteDto.class, rede.get("codigo"), rede.get("descricao"));

		var selectTabela = cb.construct(TabelaPrecoDto.class, tabela.get("codigo"), tabela.get("descricao"));

		var selectEstado = cb.construct(EstadoDto.class, estado.get("codigo"), estado.get("descricao"),
				estado.get("sigla"));

		var selectCidade = cb.construct(CidadeDto.class, cidade.get("codigo"), cidade.get("descricao"), selectEstado);

		var selectBairro = cb.construct(BairroDto.class, bairro.get("codigo"), bairro.get("descricao"));

		var selectEndereco = cb.construct(EnderecoDto.class, endereco.get("logradouro"), endereco.get("numero"),
				endereco.get("cep"), endereco.get("complemento"), selectCidade, selectBairro);

		var select = cb.construct(ClienteDto.class, root.get("codigo"), root.get("documento"), root.get("nome"),
				root.get("razaoSocial"), root.get("observacao"), root.get("telefone"), root.get("email"),
				root.get("inscricaoEstadual"), root.get("tipoPessoa"), selectEndereco, selectSegmento, selectRede,
				selectTabela);

		query.select(select).where(cb.equal(root.get("codigo"), codigo),
				cb.equal(root.get("empresa").get("codigo"), codigoEmpresa));

		var registro = entityManager.createQuery(query).getSingleResult();

		return registro;
	}

}
