package br.com.ams.sys.service;

import java.util.List;

import br.com.ams.sys.entity.Cidade;
import br.com.ams.sys.records.CidadeCriarDto;
import br.com.ams.sys.records.CidadeDto;

public interface CidadeService {
	Cidade save(Cidade entidade) throws Exception;

	Cidade findByCodigo(Long codigo) throws Exception;

	void deleteByCodigo(Long codigo);

	List<CidadeDto> pesquisarDescricao(String descricao);

	CidadeDto pesquisarDescricaoSingle(String descricao, String estadoSigla);

	CidadeDto save(CidadeCriarDto entidade) throws Exception;

	CidadeDto obterCodigo(Long codigo);
}
