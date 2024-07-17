package br.com.ams.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.entity.ConfiguracaoGrade;
import br.com.ams.sys.records.ConfiguracaoGradeDto;
import br.com.ams.sys.repository.ConfiguracaoGradeRepository;
import br.com.ams.sys.service.ConfiguracaoGradeService;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class ConfiguracaoGradeServiceImpl implements ConfiguracaoGradeService {
	@Autowired
	private ConfiguracaoGradeRepository configuracaoViewRepository;

	@Override
	public ConfiguracaoGradeDto obterCodigo(Long empresa, Long codigo) {
		var registro = configuracaoViewRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
		if(registro.getFiltros() != null) {
			registro.getFiltros().size();
		}
		return registro.toConfiguracaoViewDto();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ConfiguracaoGrade save(ConfiguracaoGrade entidade) throws Exception {
		return configuracaoViewRepository.save(entidade);
	}

	@Override
	public ConfiguracaoGrade findByCodigo(Long codigo) throws Exception {
		return configuracaoViewRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteByCodigo(Long codigo) {
		configuracaoViewRepository.deleteById(codigo);
	}
}
