package br.com.ams.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.entity.ConfiguracaoPesquisaGrade;
import br.com.ams.sys.config.RedisConfig;
import br.com.ams.sys.entity.ConfiguracaoPesquisa;
import br.com.ams.sys.records.ConfiguracaoPesquisaDto;
import br.com.ams.sys.repository.ConfiguracaoPesquisaRepository;
import br.com.ams.sys.service.CacheService;
import br.com.ams.sys.service.ConfiguracaoPesquisaService;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class ConfiguracaoPesquisaServiceImpl implements ConfiguracaoPesquisaService {
	@Autowired
	private ConfiguracaoPesquisaRepository configuracaoViewRepository;

	@Autowired
	private CacheService cacheService;

	@Override
	@Cacheable(value = RedisConfig.CACHE_CONFIG_PESQUISA_CODIGO_KEY)
	public ConfiguracaoPesquisaDto obterCodigo(Long empresa, Long codigo) {
		var registro = configuracaoViewRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
		if (registro.getFiltros() != null) {
			registro.getFiltros().size();
		}
		return registro.toConfiguracaoPesquisaDto();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ConfiguracaoPesquisa save(ConfiguracaoPesquisa entidade) throws Exception {
		cacheService.clear(RedisConfig.CACHE_CONFIG_PESQUISA_CODIGO_KEY);
		return configuracaoViewRepository.save(entidade);
	}

	@Override
	public ConfiguracaoPesquisa findByCodigo(Long codigo) throws Exception {
		return configuracaoViewRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteByCodigo(Long codigo) {
		cacheService.clear(RedisConfig.CACHE_CONFIG_PESQUISA_CODIGO_KEY);
		configuracaoViewRepository.deleteById(codigo);
	}
}
