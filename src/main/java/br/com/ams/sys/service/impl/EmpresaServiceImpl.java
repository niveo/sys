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

import br.com.ams.sys.entity.Empresa;
import br.com.ams.sys.records.BairroDto;
import br.com.ams.sys.records.CidadeDto;
import br.com.ams.sys.records.EmpresaDto;
import br.com.ams.sys.records.EmpresaListaDto;
import br.com.ams.sys.records.EmpresaRegistrarDto;
import br.com.ams.sys.records.EnderecoDto;
import br.com.ams.sys.records.EstadoDto;
import br.com.ams.sys.repository.EmpresaRepository;
import br.com.ams.sys.service.BairroService;
import br.com.ams.sys.service.CidadeService;
import br.com.ams.sys.service.EmpresaService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class EmpresaServiceImpl implements EmpresaService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private CidadeService cidadeService;

	@Autowired
	private BairroService bairroService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Empresa save(Empresa entidade) throws Exception {
		return empresaRepository.save(entidade);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public EmpresaDto save(EmpresaRegistrarDto entidade) throws Exception {

		var cidade = cidadeService.findByCodigo(entidade.endereco().cidade().codigo());
		var bairro = bairroService.findByCodigo(entidade.endereco().bairro().codigo());

		Empresa registrar;
		if (entidade.codigo() != null) {
			var emp = empresaRepository.findById(entidade.codigo()).get();
			registrar = entidade.toEmpresa(emp, cidade, bairro);
		} else {
			registrar = entidade.toEmpresa(new Empresa(), cidade, bairro);
		}

		registrar.getEndereco().setCep(registrar.getEndereco().getCep().replace("-", "").trim());

		registrar = save(registrar);

		return obterCodigo(registrar.getCodigo());
	}

	@Override 
	public Empresa findByCodigo(Long codigo) throws Exception {
		return this.empresaRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteByCodigo(Long codigo) {
		this.empresaRepository.deleteById(codigo);
	}

	public EmpresaDto obterCodigo(Long codigo) throws Exception {
		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(EmpresaDto.class);
		var root = query.from(Empresa.class);

		var endereco = root.get("endereco");
		var cidade = endereco.get("cidade");
		var estado = cidade.get("estado");
		var bairro = endereco.get("bairro");

		var selectEstado = cb.construct(EstadoDto.class, estado.get("codigo"), estado.get("descricao"),
				estado.get("sigla"));

		var selectCidade = cb.construct(CidadeDto.class, cidade.get("codigo"), cidade.get("descricao"), selectEstado);

		var selectBairro = cb.construct(BairroDto.class, bairro.get("codigo"), bairro.get("descricao"));

		var selectEndereco = cb.construct(EnderecoDto.class, endereco.get("logradouro"), endereco.get("numero"),
				endereco.get("cep"), endereco.get("complemento"), selectCidade, selectBairro);

		var select = cb.construct(EmpresaDto.class, root.get("codigo"), root.get("documento"), root.get("nome"),
				root.get("razaoSocial"), root.get("observacao"), root.get("telefone"), root.get("email"),
				root.get("inscricaoEstadual"), root.get("tipoPessoa"), selectEndereco);

		query.select(select).where(cb.equal(root.get("codigo"), codigo));

		return (EmpresaDto) entityManager.createQuery(query).getSingleResult();

	}

	@Override
	public Page<EmpresaListaDto> obterTodos(PageRequest pageable, String conditions) throws Exception {

		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(EmpresaListaDto.class);
		var root = query.from(Empresa.class);

		var select = cb.construct(EmpresaListaDto.class, root.get("codigo"), root.get("documento"), root.get("nome"),
				root.get("razaoSocial"), root.get("telefone"), root.get("email"), root.get("inscricaoEstadual"),
				root.get("tipoPessoa"));

		query.select(select);

		var predicates = obterPredicates(cb, root, conditions);
		if (predicates.length > 0)
			query.where(cb.or(predicates));

		var registros = entityManager.createQuery(query).setFirstResult((int) pageable.getOffset())
				.setMaxResults(pageable.getPageSize()).getResultList();

		return new PageImpl<EmpresaListaDto>(registros, pageable, contarRegistros(cb, conditions));

	}

	private Long contarRegistros(CriteriaBuilder cb, String conditions) throws Exception {

		// Create Count Query
		var countQuery = cb.createQuery(Long.class);
		var root = countQuery.from(Empresa.class);

		var predicates = obterPredicates(cb, root, conditions);
		if (predicates.length > 0)
			countQuery.where(cb.or(predicates));

		countQuery.select(cb.count(root));
		return entityManager.createQuery(countQuery).getSingleResult();
	}

	private Predicate[] obterPredicates(CriteriaBuilder cb, Root<Empresa> root, String conditions) throws Exception {

		JsonNode filtros = null;
		if (conditions != null && !conditions.isEmpty())
			filtros = new ObjectMapper().readTree(conditions);

		var predicates = new ArrayList<Predicate>();

		var itFiltros = filtros.fieldNames();
		while (itFiltros.hasNext()) {
			var name = itFiltros.next();
			var node = filtros.get(name);
			if ("codigo".equals(name)) {
				var predValue = cb.equal(root.get("codigo"), node.asLong());
				predicates.add(predValue);
			}
			if ("nome".equals(name)) {
				var predValue = cb.like(root.get("nome"), String.format("\"%%s\"%", node.asText()));
				predicates.add(predValue);
			}
			if ("razaoSocial".equals(name)) {
				var predValue = cb.like(root.get("razaoSocial"), String.format("\"%%s\"%", node.asText()));
				predicates.add(predValue);
			}
			if ("documento".equals(name)) {
				var predValue = cb.like(root.get("documento"), String.format("\"%%s\"%", node.asText()));
				predicates.add(predValue);
			}
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
