package br.com.ams.sys.service;

import java.util.List;

import br.com.ams.sys.entity.Bairro;
import br.com.ams.sys.records.BairroCriarDto;
import br.com.ams.sys.records.BairroDto;

public interface BairroService {
	Bairro save(Bairro entidade) throws Exception;

	Bairro findByCodigo(Long codigo) throws Exception;

	void deleteByCodigo(Long codigo) throws Exception;

	public List<BairroDto> pesquisarDescricao(String descricao);

	BairroDto criar(BairroCriarDto entity) throws Exception;
}
