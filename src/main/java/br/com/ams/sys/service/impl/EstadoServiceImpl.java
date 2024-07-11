package br.com.ams.sys.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.config.RedisConfig;
import br.com.ams.sys.entity.Estado;
import br.com.ams.sys.records.EstadoDto;
import br.com.ams.sys.repository.EstadoRepository;
import br.com.ams.sys.service.EstadoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

@Service
@Transactional(readOnly = true)
public class EstadoServiceImpl implements EstadoService {
	@Autowired
	private EstadoRepository estadoRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Estado save(Estado entidade) throws Exception {
		if (StringUtils.isNotEmpty(entidade.getDescricao()))
			entidade.setDescricao(entidade.getDescricao().toUpperCase());
		if (StringUtils.isNotEmpty(entidade.getSigla()))
			entidade.setSigla(entidade.getSigla().toUpperCase());
		return estadoRepository.save(entidade);
	}

	@Override
	public Estado findByCodigo(Long codigo) throws Exception {
		return this.estadoRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteByCodigo(Long codigo) {
		this.estadoRepository.deleteById(codigo);
	}

	@Cacheable(value = RedisConfig.CACHE_ESTADOS_KEY)
	@Override
	public List<EstadoDto> obterTodos() throws Exception {
		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(EstadoDto.class);
		var root = query.from(Estado.class);

		var select = cb.construct(EstadoDto.class, root.get("codigo"), root.get("descricao"), root.get("sigla"));

		query.select(select).orderBy(cb.asc(root.get("descricao")));

		return entityManager.createQuery(query).getResultList();
	}

	@Override
	public Estado findBySigla(String sigla) throws Exception {
		return this.estadoRepository.findBySigla(sigla);
	}

}
