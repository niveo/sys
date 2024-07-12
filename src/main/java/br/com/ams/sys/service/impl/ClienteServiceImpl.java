package br.com.ams.sys.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ams.sys.entity.Cliente;
import br.com.ams.sys.records.BairroDto;
import br.com.ams.sys.records.CidadeDto;
import br.com.ams.sys.records.ClienteDto;
import br.com.ams.sys.records.ClienteListaDto;
import br.com.ams.sys.records.ClienteRegistrarDto;
import br.com.ams.sys.records.EnderecoDto;
import br.com.ams.sys.records.EstadoDto;
import br.com.ams.sys.records.SegmentoDto;
import br.com.ams.sys.repository.ClienteRepository;
import br.com.ams.sys.security.IAuthenticationFacade;
import br.com.ams.sys.service.BairroService;
import br.com.ams.sys.service.CidadeService;
import br.com.ams.sys.service.ClienteService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClienteServiceImpl implements ClienteService {
	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private CidadeService cidadeService;

	@Autowired
	private BairroService bairroService;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private IAuthenticationFacade authentication;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Cliente save(Cliente entidade) throws Exception {
		return clienteRepository.save(entidade);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ClienteDto save(ClienteRegistrarDto entidade) throws Exception {
		var cidade = cidadeService.findByCodigo(entidade.endereco().cidade().codigo());
		var bairro = bairroService.findByCodigo(entidade.endereco().bairro().codigo());

		Cliente registrar;
		if (entidade.codigo() != null) {
			var emp = clienteRepository.findById(entidade.codigo()).get();
			registrar = entidade.toCliente(emp, cidade, bairro);
		} else {
			registrar = entidade.toCliente(new Cliente(), cidade, bairro);
		}

		registrar.getEndereco().setCep(registrar.getEndereco().getCep().replace("-", "").trim());

		registrar = save(registrar);

		return obterCodigo(registrar.getCodigo());
	}

	@Override
	public Cliente findByCodigo(Long codigo) throws Exception {
		return this.clienteRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteByCodigo(Long codigo) {
		this.clienteRepository.deleteById(codigo);
	}

	@Override
	public Page<ClienteListaDto> obterTodos(Long empresa, PageRequest pageable, String conditions) throws Exception {

		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(ClienteListaDto.class);
		var root = query.from(Cliente.class);

		var select = cb.construct(ClienteListaDto.class, root.get("codigo"), root.get("documento"), root.get("nome"),
				root.get("razaoSocial"), root.get("telefone"), root.get("email"), root.get("inscricaoEstadual"),
				root.get("tipoPessoa"));

		query.select(select);

		var predicates = obterPredicates(cb, root, conditions);
		if (predicates.length > 0)
			query.where(predicates);

		var registros = entityManager.createQuery(query).setFirstResult((int) pageable.getOffset())
				.setMaxResults(pageable.getPageSize()).getResultList();

		return new PageImpl<ClienteListaDto>(registros, pageable, contarRegistros(cb, conditions));
	}

	private Long contarRegistros(CriteriaBuilder cb, String conditions) throws Exception {

		// Create Count Query
		var countQuery = cb.createQuery(Long.class);
		var root = countQuery.from(Cliente.class);

		var predicates = obterPredicates(cb, root, conditions);
		if (predicates.length > 0)
			countQuery.where(predicates);

		countQuery.select(cb.count(root));
		return entityManager.createQuery(countQuery).getSingleResult();
	}

	private Predicate[] obterPredicates(CriteriaBuilder cb, Root<Cliente> root, String conditions) throws Exception {

		JsonNode filtros = null;
		if (conditions != null && !conditions.isEmpty())
			filtros = new ObjectMapper().readTree(conditions);

		var predicates = new ArrayList<Predicate>();

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
				var predValue = cb.like(root.get("nome"), "'%" + node.asText() + "%'");
				predicates.add(cb.or(predValue));
			}
			if ("razaoSocial".equals(name)) {
				var predValue = cb.like(root.get("razaoSocial"), "'%" + node.asText() + "%'");
				predicates.add(cb.or(predValue));
			}
			if ("documento".equals(name)) {
				var predValue = cb.like(root.get("documento"), "'%" + node.asText() + "%'");
				predicates.add(cb.or(predValue));
			}
		}

		var userDetails = authentication.getUserDetails();
		var empresaCodigo = authentication.getUserDetails().getEmpresaCodigo();
		log.warn("Empresa autenticada {} para usuário {} logado.", empresaCodigo, userDetails.getUsername());
		if (empresaCodigo != null) {
			var predValue = cb.equal(root.get("empresa").get("codigo"), empresaCodigo);
			predicates.add(predValue);
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	public ClienteDto obterCodigo(Long codigo) throws Exception {
		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(ClienteDto.class);
		var root = query.from(Cliente.class);

		var segmento = root.join("segmento", JoinType.LEFT);

		var endereco = root.get("endereco");
		var cidade = endereco.get("cidade");
		var estado = cidade.get("estado");
		var bairro = endereco.get("bairro");

		var selectSegmento = cb.construct(SegmentoDto.class, segmento.get("codigo"), segmento.get("descricao"));

		var selectEstado = cb.construct(EstadoDto.class, estado.get("codigo"), estado.get("descricao"),
				estado.get("sigla"));

		var selectCidade = cb.construct(CidadeDto.class, cidade.get("codigo"), cidade.get("descricao"), selectEstado);

		var selectBairro = cb.construct(BairroDto.class, bairro.get("codigo"), bairro.get("descricao"));

		var selectEndereco = cb.construct(EnderecoDto.class, endereco.get("logradouro"), endereco.get("numero"),
				endereco.get("cep"), endereco.get("complemento"), selectCidade, selectBairro);

		var select = cb.construct(ClienteDto.class, root.get("codigo"), root.get("documento"), root.get("nome"),
				root.get("razaoSocial"), root.get("observacao"), root.get("telefone"), root.get("email"),
				root.get("inscricaoEstadual"), root.get("tipoPessoa"), selectSegmento, selectEndereco);

		query.select(select).where(cb.equal(root.get("codigo"), codigo));

		return (ClienteDto) entityManager.createQuery(query).getSingleResult();
	}

}
