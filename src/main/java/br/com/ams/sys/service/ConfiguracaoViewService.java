package br.com.ams.sys.service;

import br.com.ams.sys.entity.ConfiguracaoView;
import br.com.ams.sys.records.ConfiguracaoViewDto;

public interface ConfiguracaoViewService {

	ConfiguracaoViewDto obterCodigo(Long empresa, Long codigo);

	ConfiguracaoView save(ConfiguracaoView entidade) throws Exception;

	ConfiguracaoView findByCodigo(Long codigo) throws Exception;

	void deleteByCodigo(Long codigo);
}
