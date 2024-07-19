package br.com.ams.sys.service.impl;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ams.sys.common.Constante;
import br.com.ams.sys.common.RestPage;
import br.com.ams.sys.config.RedisConfig;
import br.com.ams.sys.entity.Cidade;
import br.com.ams.sys.records.CidadeCriarDto;
import br.com.ams.sys.records.CidadeDto;
import br.com.ams.sys.records.EstadoDto;
import br.com.ams.sys.repository.CidadeRepository;
import br.com.ams.sys.service.CacheService;
import br.com.ams.sys.service.CidadeService;
import br.com.ams.sys.service.EstadoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
@Transactional
public class CidadeServiceImpl implements CidadeService {
	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private EstadoService estadoService;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private CacheService cacheService;

	@Override
	public Cidade save(Cidade entidade) throws Exception {
		if (StringUtils.isNotEmpty(entidade.getDescricao()))
			entidade.setDescricao(entidade.getDescricao().toUpperCase());
		cacheService.clear(RedisConfig.CACHE_CIDADE_KEY);
		return cidadeRepository.save(entidade);
	}

	@Override
	public Cidade findByCodigo(Long codigo) throws Exception {
		return this.cidadeRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
	}

	@Override
	public CidadeDto save(CidadeCriarDto entidade) throws Exception {
		var estado = estadoService.findByCodigo(entidade.estado());
		var cidade = save(Cidade.builder().descricao(entidade.descricao()).estado(estado).build());
		return obterCodigo(cidade.getCodigo());
	}

	@Override
	public void deleteByCodigo(Long codigo) {
		this.cidadeRepository.deleteById(codigo);
	}

	@Override
	@Cacheable(value = RedisConfig.CACHE_CIDADE_KEY)
	public Page<CidadeDto> obterTodos(Integer page, String conditions) throws Exception {
		page--;
		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(CidadeDto.class);
		var root = query.from(Cidade.class);

		var estado = root.get("estado");

		var selectEstado = cb.construct(EstadoDto.class, estado.get("codigo"), estado.get("descricao"),
				estado.get("sigla"));

		var select = cb.construct(CidadeDto.class, root.get("codigo"), root.get("descricao"), selectEstado);

		query.select(select);

		var predicates = obterPredicates(cb, root, conditions);

		if (predicates.length > 0)
			query.where(predicates);

		query.orderBy(cb.asc(root.get("descricao")));

		var registros = entityManager.createQuery(query).setFirstResult(page * Constante.PAGINA_REGISTROS)
				.setMaxResults(Constante.PAGINA_REGISTROS).getResultList();

		var contaRegistros = contarRegistros(cb, conditions);

		return new RestPage<CidadeDto>(registros, page, Constante.PAGINA_REGISTROS, contaRegistros);
	}

	private Long contarRegistros(CriteriaBuilder cb, String conditions) throws Exception {

		// Create Count Query
		var countQuery = cb.createQuery(Long.class);
		var root = countQuery.from(Cidade.class);

		var predicates = obterPredicates(cb, root, conditions);
		if (predicates.length > 0)
			countQuery.where(predicates);

		countQuery.select(cb.count(root));
		return entityManager.createQuery(countQuery).getSingleResult();
	}

	private Predicate[] obterPredicates(CriteriaBuilder cb, Root<Cidade> root, String conditions) throws Exception {

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
				if ("codigo".equals(name)) {
					var predValue = cb.equal(root.get("codigo"), node.asLong());
					predicates.add(predValue);
				}
				if ("estado".equals(name)) {
					predicates.add(
							cb.or(cb.like(root.get("estado").get("descricao"), "%" + node.asText().toUpperCase() + "%"),
									cb.like(root.get("estado").get("sigla"), "%" + node.asText().toUpperCase() + "%")));
				}
			}
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public CidadeDto pesquisarDescricaoSingle(String descricao, String estadoSigla) {
		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(CidadeDto.class);
		var root = query.from(Cidade.class);

		var estado = root.get("estado");

		var selectEstado = cb.construct(EstadoDto.class, estado.get("codigo"), estado.get("descricao"),
				estado.get("sigla"));

		var select = cb.construct(CidadeDto.class, root.get("codigo"), root.get("descricao"), selectEstado);

		query.select(select)
				.where(cb.equal(root.get("descricao"), descricao.toUpperCase()),
						cb.and(cb.equal(estado.get("sigla"), estadoSigla.toUpperCase())))
				.orderBy(cb.asc(root.get("descricao")));

		return entityManager.createQuery(query).setMaxResults(1).getSingleResult();
	}

	@Override
	public CidadeDto obterCodigo(Long codigo) {

		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(CidadeDto.class);
		var root = query.from(Cidade.class);

		var estado = root.get("estado");

		var selectEstado = cb.construct(EstadoDto.class, estado.get("codigo"), estado.get("descricao"),
				estado.get("sigla"));

		var select = cb.construct(CidadeDto.class, root.get("codigo"), root.get("descricao"), selectEstado);

		query.select(select).where(cb.equal(root.get("codigo"), codigo));

		return entityManager.createQuery(query).getSingleResult();
	}
}
