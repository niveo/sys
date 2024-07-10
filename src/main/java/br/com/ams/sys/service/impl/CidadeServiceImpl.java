package br.com.ams.sys.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.config.RedisConfig;
import br.com.ams.sys.entity.Cidade;
import br.com.ams.sys.records.CidadeCriarDto;
import br.com.ams.sys.records.CidadeDto;
import br.com.ams.sys.records.EstadoDto;
import br.com.ams.sys.repository.CidadeRepository;
import br.com.ams.sys.service.CidadeService;
import br.com.ams.sys.service.EstadoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

@Service
@Transactional
public class CidadeServiceImpl implements CidadeService {
	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private EstadoService estadoService;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Cidade save(Cidade entidade) throws Exception {
		if (StringUtils.isNotEmpty(entidade.getDescricao()))
			entidade.setDescricao(entidade.getDescricao().toUpperCase());
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
	public List<CidadeDto> pesquisarDescricao(String descricao) {

		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(CidadeDto.class);
		var root = query.from(Cidade.class);

		var estado = root.get("estado");

		var selectEstado = cb.construct(EstadoDto.class, estado.get("codigo"), estado.get("descricao"),
				estado.get("sigla"));

		var select = cb.construct(CidadeDto.class, root.get("codigo"), root.get("descricao"), selectEstado);

		query.select(select).where(cb.like(root.get("descricao"), "%" + descricao.toUpperCase() + "%"))
				.orderBy(cb.asc(root.get("descricao")));

		return entityManager.createQuery(query).getResultList();
	}

	@Override
	public CidadeDto pesquisarDescricaoSingle(String descricao, String estadoSigla) {

		System.out.println(descricao);
		System.out.println(estadoSigla);

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
