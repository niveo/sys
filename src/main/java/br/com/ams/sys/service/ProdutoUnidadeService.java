package br.com.ams.sys.service;

import java.util.List;

import org.springframework.data.domain.Page;

import br.com.ams.sys.entity.ProdutoUnidade;
import br.com.ams.sys.records.ProdutoUnidadeDto;

public interface ProdutoUnidadeService {
	Page<ProdutoUnidadeDto> findByProduto(Integer page, Long codigo);

	ProdutoUnidade save(ProdutoUnidade registro) throws Exception;

	ProdutoUnidadeDto save(ProdutoUnidadeDto registro) throws Exception;

	void deleteByCodigo(Long codigo);
}
