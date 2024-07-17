package br.com.ams.sys.service;

import br.com.ams.sys.entity.ConfiguracaoGrade;
import br.com.ams.sys.records.ConfiguracaoGradeDto;

public interface ConfiguracaoGradeService {

	ConfiguracaoGradeDto obterCodigo(Long empresa, Long codigo);

	ConfiguracaoGrade save(ConfiguracaoGrade entidade) throws Exception;

	ConfiguracaoGrade findByCodigo(Long codigo) throws Exception;

	void deleteByCodigo(Long codigo);
}
