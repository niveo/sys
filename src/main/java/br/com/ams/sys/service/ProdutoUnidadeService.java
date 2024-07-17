package br.com.ams.sys.service;

import java.util.List;

import br.com.ams.sys.entity.ProdutoUnidade;
import br.com.ams.sys.records.ProdutoUnidadeDto;

public interface ProdutoUnidadeService {
	List<ProdutoUnidadeDto> findByProduto(Long codigo);

	ProdutoUnidade save(ProdutoUnidade registro) throws Exception;

	ProdutoUnidadeDto save(ProdutoUnidadeDto registro) throws Exception;

	void deleteByCodigo(Long codigo);
}
