package br.com.ams.sys.service;

import java.util.List;

import org.springframework.data.domain.Page;

import br.com.ams.sys.entity.ProdutoUnidade;
import br.com.ams.sys.entity.TabelaPrecoLancamento;
import br.com.ams.sys.entity.TabelaPrecoProduto;
import br.com.ams.sys.records.ProdutoUnidadeDto;
import br.com.ams.sys.records.TabelaPrecoLancamentoDto;
import br.com.ams.sys.records.TabelaPrecoProdutoDto;
import br.com.ams.sys.records.TabelaPrecoProdutoRegisterDto;

public interface TabelaPrecoProdutoService {
	Page<TabelaPrecoProdutoDto> findByLancamento(Integer page, Long codigo);

	TabelaPrecoProduto save(TabelaPrecoProduto registro) throws Exception;

	TabelaPrecoProdutoDto save(TabelaPrecoProdutoRegisterDto registro) throws Exception;

	void deleteByCodigo(Long codigo);
}
