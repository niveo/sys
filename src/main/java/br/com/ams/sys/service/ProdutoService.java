package br.com.ams.sys.service;

import org.springframework.data.domain.Page;

import br.com.ams.sys.entity.Produto;
import br.com.ams.sys.records.ProdutoDto;
import br.com.ams.sys.records.ProdutoListaDto;

public interface ProdutoService {
	Produto save(Produto entidade) throws Exception;

	Produto findByCodigo(Long codigo) throws Exception;

	void deleteByCodigo(Long codigo);

	Page<ProdutoListaDto> obterTodos(Long empresa, Integer page, String conditions) throws Exception;

	ProdutoDto save(Long empresa, ProdutoDto entidade) throws Exception;

	ProdutoDto obterCodigo(Long empresa, Long codigo);
}
