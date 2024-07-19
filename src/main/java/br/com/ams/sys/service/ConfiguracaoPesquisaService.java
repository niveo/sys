package br.com.ams.sys.service;

import br.com.ams.sys.entity.ConfiguracaoPesquisaGrade;
import br.com.ams.sys.entity.ConfiguracaoPesquisa;
import br.com.ams.sys.records.ConfiguracaoPesquisaDto;

public interface ConfiguracaoPesquisaService {

	ConfiguracaoPesquisaDto obterCodigo(Long empresa, Long codigo);

	ConfiguracaoPesquisa save(ConfiguracaoPesquisa entidade) throws Exception;

	ConfiguracaoPesquisa findByCodigo(Long codigo) throws Exception;

	void deleteByCodigo(Long codigo);
}
