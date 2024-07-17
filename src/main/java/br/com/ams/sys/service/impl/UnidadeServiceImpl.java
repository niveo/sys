package br.com.ams.sys.service.impl;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
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
import br.com.ams.sys.entity.Unidade;
import br.com.ams.sys.records.UnidadeDto;
import br.com.ams.sys.repository.UnidadeRepository;
import br.com.ams.sys.service.CacheService;
import br.com.ams.sys.service.EmpresaService;
import br.com.ams.sys.service.UnidadeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
@Transactional(readOnly = true)
public class UnidadeServiceImpl implements UnidadeService {
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private UnidadeRepository unidadeRepository;

	@Autowired
	private EmpresaService empresaService;

	@Autowired
	private CacheService cacheService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Unidade save(Unidade entidade) throws Exception {
		if (StringUtils.isNotEmpty(entidade.getDescricao()))
			entidade.setDescricao(entidade.getDescricao().toUpperCase());

		if (StringUtils.isNotEmpty(entidade.getSigla()))
			entidade.setSigla(entidade.getSigla().toUpperCase());

		cacheService.clear(RedisConfig.CACHE_UNIDADES_KEY);

		return unidadeRepository.save(entidade);

	}

	@Override
	public Unidade findByCodigo(Long codigo) throws Exception {
		return this.unidadeRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteByCodigo(Long codigo) {
		cacheService.clear(RedisConfig.CACHE_UNIDADES_KEY);
		this.unidadeRepository.deleteById(codigo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public UnidadeDto save(Long codigoEmpresa, UnidadeDto entity) throws Exception {

		Unidade registrar;

		var empresaEntity = empresaService.findByCodigo(codigoEmpresa);

		if (entity.codigo() != null) {
			var emp = unidadeRepository.findById(entity.codigo()).get();
			registrar = entity.toUnidade(emp, empresaEntity);
		} else {
			registrar = entity.toUnidade(new Unidade(), empresaEntity);
		}

		registrar = save(registrar);

		return obterCodigo(codigoEmpresa, registrar.getCodigo());
	}

	@Cacheable(value = RedisConfig.CACHE_UNIDADES_KEY)
	public Page<UnidadeDto> obterTodos(Long codigoEmpresa, Integer page, String conditions) throws Exception {
		page--;

		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(UnidadeDto.class);
		var root = query.from(Unidade.class);

		var select = cb.construct(UnidadeDto.class, root.get("codigo"), root.get("descricao"), root.get("sigla"));

		query.select(select);

		var predicates = obterPredicates(cb, root, codigoEmpresa, conditions);
		if (predicates.length > 0)
			query.where(predicates);

		query.orderBy(cb.asc(root.get("descricao")));

		var registros = entityManager.createQuery(query).setFirstResult(page * Constante.PAGINA_REGISTROS)
				.setMaxResults(Constante.PAGINA_REGISTROS).getResultList();

		return new RestPage<UnidadeDto>(registros, page, Constante.PAGINA_REGISTROS,
				contarRegistros(cb, codigoEmpresa, conditions));
	}

	private Predicate[] obterPredicates(CriteriaBuilder cb, Root<Unidade> root, Long empresaCodigo, String conditions)
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

				if ("descricao".equals(name)) {
					var predValue = cb.like(root.get("descricao"), "%" + node.asText().toUpperCase() + "%");
					predicates.add(predValue);
				}
			}
		}

		if (empresaCodigo != null) {
			var predValue = cb.equal(root.get("empresa").get("codigo"), empresaCodigo);
			predicates.add(predValue);
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private Long contarRegistros(CriteriaBuilder cb, Long codigoEmpresa, String conditions) throws Exception {

		// Create Count Query
		var countQuery = cb.createQuery(Long.class);
		var root = countQuery.from(Unidade.class);

		var predicates = obterPredicates(cb, root, codigoEmpresa, conditions);
		if (predicates.length > 0)
			countQuery.where(predicates);

		countQuery.select(cb.count(root));
		return entityManager.createQuery(countQuery).getSingleResult();
	}

	@Override
	public UnidadeDto obterCodigo(Long codigoEmpresa, Long codigo) {

		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(UnidadeDto.class);
		var root = query.from(Unidade.class);

		var select = cb.construct(UnidadeDto.class, root.get("codigo"), root.get("descricao"), root.get("sigla"));

		query.select(select).where(cb.equal(root.get("codigo"), codigo),
				cb.equal(root.get("empresa").get("codigo"), codigoEmpresa));

		return entityManager.createQuery(query).getSingleResult();
	}
}
