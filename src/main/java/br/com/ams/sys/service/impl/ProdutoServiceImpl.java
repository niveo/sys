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
import br.com.ams.sys.entity.Produto;
import br.com.ams.sys.records.ProdutoDto;
import br.com.ams.sys.repository.ProdutoRepository;
import br.com.ams.sys.service.CacheService;
import br.com.ams.sys.service.EmpresaService;
import br.com.ams.sys.service.ProdutoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
@Transactional(readOnly = true)
public class ProdutoServiceImpl implements ProdutoService {
	@Autowired
	private ProdutoRepository produtoRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private CacheService cacheService;

	@Autowired
	private EmpresaService empresaService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Produto save(Produto entidade) throws Exception {
		cacheService.clear(RedisConfig.CACHE_PRODUTO_KEY);
		return produtoRepository.save(entidade);
	}

	@Override
	public Produto findByCodigo(Long codigo) throws Exception {
		return this.produtoRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteByCodigo(Long codigo) {
		cacheService.clear(RedisConfig.CACHE_PRODUTO_KEY);
		this.produtoRepository.deleteById(codigo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ProdutoDto save(Long codigoEmpresa, ProdutoDto entity) throws Exception {

		Produto registrar;

		var empresaEntity = empresaService.findByCodigo(codigoEmpresa);

		if (entity.codigo() != null) {
			var emp = produtoRepository.findById(entity.codigo()).get();
			registrar = entity.toProduto(emp, empresaEntity);
		} else {
			registrar = entity.toProduto(new Produto(), empresaEntity);
		}

		registrar = save(registrar);

		return obterCodigo(codigoEmpresa, registrar.getCodigo());
	}

	@Cacheable(value = RedisConfig.CACHE_PRODUTO_KEY)
	public Page<ProdutoDto> obterTodos(Long codigoEmpresa, Integer page, String conditions) throws Exception {
		page--;

		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(ProdutoDto.class);
		var root = query.from(Produto.class);

		var select = cb.construct(ProdutoDto.class, root.get("codigo"), root.get("descricao"), root.get("referencia"));

		query.select(select);

		var predicates = obterPredicates(cb, root, codigoEmpresa, conditions);
		if (predicates.length > 0)
			query.where(predicates);

		query.orderBy(cb.asc(root.get("descricao")));

		var registros = entityManager.createQuery(query).setFirstResult(page * Constante.PAGINA_REGISTROS)
				.setMaxResults(Constante.PAGINA_REGISTROS).getResultList();

		return new RestPage<ProdutoDto>(registros, page, Constante.PAGINA_REGISTROS,
				contarRegistros(cb, codigoEmpresa, conditions));
	}

	private Predicate[] obterPredicates(CriteriaBuilder cb, Root<Produto> root, Long empresaCodigo, String conditions)
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

				switch (name) {
				case "codigo": {
					var predValue = cb.equal(root.get("codigo"), node.asLong());
					predicates.add(cb.or(predValue));
					break;
				}
				case "descricao": {
					var predValue = cb.like(root.get("descricao"), "%" + node.asText().toUpperCase() + "%");
					predicates.add(predValue);
					break;
				}
				case "referencia": {
					var predValue = cb.like(root.get("referencia"), "%" + node.asText().toUpperCase() + "%");
					predicates.add(predValue);
					break;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + name);
				}

			}
		}

		if (empresaCodigo != null)

		{
			var predValue = cb.equal(root.get("empresa").get("codigo"), empresaCodigo);
			predicates.add(predValue);
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private Long contarRegistros(CriteriaBuilder cb, Long codigoEmpresa, String conditions) throws Exception {

		// Create Count Query
		var countQuery = cb.createQuery(Long.class);
		var root = countQuery.from(Produto.class);

		var predicates = obterPredicates(cb, root, codigoEmpresa, conditions);
		if (predicates.length > 0)
			countQuery.where(predicates);

		countQuery.select(cb.count(root));
		return entityManager.createQuery(countQuery).getSingleResult();
	}

	@Override
	public ProdutoDto obterCodigo(Long codigoEmpresa, Long codigo) {

		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(ProdutoDto.class);
		var root = query.from(Produto.class);

		var select = cb.construct(ProdutoDto.class, root.get("codigo"), root.get("descricao"), root.get("referencia"));

		query.select(select).where(cb.equal(root.get("codigo"), codigo),
				cb.equal(root.get("empresa").get("codigo"), codigoEmpresa));

		return entityManager.createQuery(query).getSingleResult();
	}
}
