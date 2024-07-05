package br.com.ams.sys.service;

import java.util.List;

import br.com.ams.sys.entity.Cidade;
import br.com.ams.sys.records.CidadeDto;

public interface CidadeService {
	Cidade save(Cidade entidade) throws Exception;

	Cidade findByCodigo(Long codigo) throws Exception;

	void deleteByCodigo(Long codigo) throws Exception;

	public List<CidadeDto> pesquisarDescricao(String descricao);
}
