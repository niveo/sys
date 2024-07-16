package br.com.ams.sys.service;

import org.springframework.data.domain.Page;

import br.com.ams.sys.entity.Produto;
import br.com.ams.sys.records.ProdutoDto;

public interface ProdutoService {
	Produto save(Produto entidade) throws Exception;

	Produto findByCodigo(Long codigo) throws Exception;

	void deleteByCodigo(Long codigo);

	Page<ProdutoDto> obterTodos(Long empresa, Integer page, String conditions) throws Exception;

	ProdutoDto save(Long empresa, ProdutoDto entidade) throws Exception;

	ProdutoDto obterCodigo(Long empresa, Long codigo);
}
