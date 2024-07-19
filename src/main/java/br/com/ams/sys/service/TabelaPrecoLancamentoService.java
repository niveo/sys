package br.com.ams.sys.service;

import java.util.List;

import org.springframework.data.domain.Page;

import br.com.ams.sys.entity.ProdutoUnidade;
import br.com.ams.sys.entity.SegmentoCliente;
import br.com.ams.sys.entity.TabelaPrecoLancamento;
import br.com.ams.sys.records.ProdutoUnidadeDto;
import br.com.ams.sys.records.TabelaPrecoLancamentoDto;

public interface TabelaPrecoLancamentoService {
	Page<TabelaPrecoLancamentoDto> findByProduto(Integer page, Long codigo);

	TabelaPrecoLancamento findByCodigo(Long codigo) throws Exception;

	TabelaPrecoLancamento save(TabelaPrecoLancamento registro) throws Exception;

	TabelaPrecoLancamentoDto save(TabelaPrecoLancamentoDto registro) throws Exception;

	void deleteByCodigo(Long codigo);
}
