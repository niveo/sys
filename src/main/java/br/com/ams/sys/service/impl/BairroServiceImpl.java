package br.com.ams.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.config.RedisConfig;
import br.com.ams.sys.entity.Bairro;
import br.com.ams.sys.records.BairroDto;
import br.com.ams.sys.repository.BairroRepository;
import br.com.ams.sys.service.BairroService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

@Service
@Transactional
public class BairroServiceImpl implements BairroService {

	@Autowired
	private BairroRepository bairroRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Bairro save(Bairro entidade) throws Exception {
		return bairroRepository.save(entidade);
	}

	@Override
	public Bairro findByCodigo(Long codigo) throws Exception {
		return this.bairroRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
	}

	@Override
	public void deleteByCodigo(Long codigo) throws Exception {
		this.bairroRepository.deleteById(codigo);
	}

	@Cacheable(value = RedisConfig.CACHE_CIDADE_KEY)
	public List<BairroDto> pesquisarDescricao(String descricao) {
		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(BairroDto.class);
		var root = query.from(Bairro.class);

		var select = cb.construct(BairroDto.class, root.get("codigo"), root.get("descricao"));

		query.select(select).where(cb.like(root.get("descricao"), "%" + descricao.toUpperCase() + "%"));

		return entityManager.createQuery(query).getResultList();
	}
}
