package br.com.ams.sys.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.config.RedisConfig;
import br.com.ams.sys.entity.Bairro;
import br.com.ams.sys.records.BairroCriarDto;
import br.com.ams.sys.records.BairroDto;
import br.com.ams.sys.repository.BairroRepository;
import br.com.ams.sys.service.BairroService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

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
	public List<BairroDto> pesquisarDescricao(String descricao) {
		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(BairroDto.class);
		var root = query.from(Bairro.class);

		var select = cb.construct(BairroDto.class, root.get("codigo"), root.get("descricao"));

		query.select(select).where(cb.like(root.get("descricao"), "%" + descricao.toUpperCase() + "%"))
				.orderBy(cb.asc(root.get("descricao")));

		return entityManager.createQuery(query).getResultList();
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
