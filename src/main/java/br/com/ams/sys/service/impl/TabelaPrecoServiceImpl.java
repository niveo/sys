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
import br.com.ams.sys.entity.TabelaPreco;
import br.com.ams.sys.records.TabelaPrecoDto;
import br.com.ams.sys.repository.TabelaPrecoRepository;
import br.com.ams.sys.service.CacheService;
import br.com.ams.sys.service.EmpresaService;
import br.com.ams.sys.service.TabelaPrecoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
@Transactional(readOnly = true)
public class TabelaPrecoServiceImpl implements TabelaPrecoService {
	@Autowired
	private TabelaPrecoRepository tabelaPrecoRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private CacheService cacheService;

	@Autowired
	private EmpresaService empresaService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TabelaPreco save(TabelaPreco entidade) throws Exception {
		cacheService.clear(RedisConfig.CACHE_TABELA_PRECO_KEY);
		return tabelaPrecoRepository.save(entidade);
	}

	@Override
	public TabelaPreco findByCodigo(Long codigo) throws Exception {
		return this.tabelaPrecoRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteByCodigo(Long codigo) {
		cacheService.clear(RedisConfig.CACHE_TABELA_PRECO_KEY);
		this.tabelaPrecoRepository.deleteById(codigo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TabelaPrecoDto save(Long codigoEmpresa, TabelaPrecoDto entity) throws Exception {

		TabelaPreco registrar;

		var empresaEntity = empresaService.findByCodigo(codigoEmpresa);

		if (entity.codigo() != null) {
			var emp = tabelaPrecoRepository.findById(entity.codigo()).get();
			registrar = entity.toTabelaPreco(emp, empresaEntity);
		} else {
			registrar = entity.toTabelaPreco(new TabelaPreco(), empresaEntity);
		}

		registrar = save(registrar);

		return obterCodigo(codigoEmpresa, registrar.getCodigo());
	}

	@Cacheable(value = RedisConfig.CACHE_TABELA_PRECO_KEY)
	public Page<TabelaPrecoDto> obterTodos(Long codigoEmpresa, Integer page, String conditions) throws Exception {
		page--;

		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(TabelaPrecoDto.class);
		var root = query.from(TabelaPreco.class);

		var select = cb.construct(TabelaPrecoDto.class, root.get("codigo"), root.get("descricao"));

		query.select(select);

		var predicates = obterPredicates(cb, root, codigoEmpresa, conditions);
		if (predicates.length > 0)
			query.where(predicates);

		query.orderBy(cb.asc(root.get("descricao")));

		var registros = entityManager.createQuery(query).setFirstResult(page * Constante.PAGINA_REGISTROS)
				.setMaxResults(Constante.PAGINA_REGISTROS).getResultList();

		return new RestPage<TabelaPrecoDto>(registros, page, Constante.PAGINA_REGISTROS,
				contarRegistros(cb, codigoEmpresa, conditions));
	}

	private Predicate[] obterPredicates(CriteriaBuilder cb, Root<TabelaPreco> root, Long empresaCodigo,
			String conditions) throws Exception {

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
		var root = countQuery.from(TabelaPreco.class);

		var predicates = obterPredicates(cb, root, codigoEmpresa, conditions);
		if (predicates.length > 0)
			countQuery.where(predicates);

		countQuery.select(cb.count(root));
		return entityManager.createQuery(countQuery).getSingleResult();
	}

	@Override
	public TabelaPrecoDto obterCodigo(Long codigoEmpresa, Long codigo) {

		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(TabelaPrecoDto.class);
		var root = query.from(TabelaPreco.class);

		var select = cb.construct(TabelaPrecoDto.class, root.get("codigo"), root.get("descricao"));

		query.select(select).where(cb.equal(root.get("codigo"), codigo),
				cb.equal(root.get("empresa").get("codigo"), codigoEmpresa));

		return entityManager.createQuery(query).getSingleResult();
	}
}
