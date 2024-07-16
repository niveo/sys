package br.com.ams.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.entity.ConfiguracaoView;
import br.com.ams.sys.records.ConfiguracaoViewDto;
import br.com.ams.sys.repository.ConfiguracaoViewRepository;
import br.com.ams.sys.service.ConfiguracaoViewService;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class ConfiguracaoViewServiceImpl implements ConfiguracaoViewService {
	@Autowired
	private ConfiguracaoViewRepository configuracaoViewRepository;

	@Override
	public ConfiguracaoViewDto obterCodigo(Long empresa, Long codigo) {
		var registro = configuracaoViewRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
		return registro.toConfiguracaoViewDto();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ConfiguracaoView save(ConfiguracaoView entidade) throws Exception {
		return configuracaoViewRepository.save(entidade);
	}

	@Override
	public ConfiguracaoView findByCodigo(Long codigo) throws Exception {
		return configuracaoViewRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteByCodigo(Long codigo) {
		configuracaoViewRepository.deleteById(codigo);
	}
}
