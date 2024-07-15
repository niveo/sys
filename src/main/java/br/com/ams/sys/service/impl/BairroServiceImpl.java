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
import br.com.ams.sys.entity.Bairro;
import br.com.ams.sys.records.BairroCriarDto;
import br.com.ams.sys.records.BairroDto;
import br.com.ams.sys.repository.BairroRepository;
import br.com.ams.sys.service.BairroService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
@Transactional(readOnly = true)
public class BairroServiceImpl implements BairroService {

	@Autowired
	private BairroRepository bairroRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Bairro save(Bairro entidade) throws Exception {
		if (StringUtils.isNotEmpty(entidade.getDescricao()))
			entidade.setDescricao(entidade.getDescricao().toUpperCase());
		return bairroRepository.save(entidade);
	}

	@Override
	public Bairro findByCodigo(Long codigo) throws Exception {
		return this.bairroRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteByCodigo(Long codigo) {
		this.bairroRepository.deleteById(codigo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public BairroDto save(BairroCriarDto entity) throws Exception {
		var bairro = save(Bairro.builder().descricao(entity.descricao()).build());
		return obterCodigo(bairro.getCodigo());
	}

	@Cacheable(value = RedisConfig.CACHE_BAIRRO_KEY)
	public Page<BairroDto> obterTodos(Integer page, String conditions) throws Exception {
		page--;

		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(BairroDto.class);
		var root = query.from(Bairro.class);

		var select = cb.construct(BairroDto.class, root.get("codigo"), root.get("descricao"));

		query.select(select);

		var predicates = obterPredicates(cb, root, conditions);
		if (predicates.length > 0)
			query.where(predicates);

		query.orderBy(cb.asc(root.get("descricao")));

		var registros = entityManager.createQuery(query).setFirstResult(page * Constante.PAGINA_REGISTROS)
				.setMaxResults(Constante.PAGINA_REGISTROS).getResultList();

		return new RestPage<BairroDto>(registros, page, Constante.PAGINA_REGISTROS, contarRegistros(cb, conditions));
	}

	private Predicate[] obterPredicates(CriteriaBuilder cb, Root<Bairro> root, String conditions) throws Exception {

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
					var predValue = cb.like(root.get("descricao"), "%" + node.asText() + "%");
					predicates.add(predValue);
				}
			}
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private Long contarRegistros(CriteriaBuilder cb, String conditions) throws Exception {

		// Create Count Query
		var countQuery = cb.createQuery(Long.class);
		var root = countQuery.from(Bairro.class);

		var predicates = obterPredicates(cb, root, conditions);
		if (predicates.length > 0)
			countQuery.where(predicates);

		countQuery.select(cb.count(root));
		return entityManager.createQuery(countQuery).getSingleResult();
	}

	public BairroDto pesquisarDescricaoSingle(String descricao) {
		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(BairroDto.class);
		var root = query.from(Bairro.class);

		var select = cb.construct(BairroDto.class, root.get("codigo"), root.get("descricao"));

		query.select(select).where(cb.equal(root.get("descricao"), descricao.toUpperCase()))
				.orderBy(cb.asc(root.get("descricao")));

		return entityManager.createQuery(query).setMaxResults(1).getSingleResult();
	}

	@Override
	public BairroDto obterCodigo(Long codigo) {

		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(BairroDto.class);
		var root = query.from(Bairro.class);

		var select = cb.construct(BairroDto.class, root.get("codigo"), root.get("descricao"));

		query.select(select).where(cb.equal(root.get("codigo"), codigo));

		return entityManager.createQuery(query).getSingleResult();
	}
}
