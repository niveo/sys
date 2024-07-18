package br.com.ams.sys.service;

import org.springframework.data.domain.Page;

import br.com.ams.sys.entity.TabelaPreco;
import br.com.ams.sys.records.TabelaPrecoDto;
import br.com.ams.sys.records.TabelaPrecoListaDto;
import br.com.ams.sys.records.TabelaPrecoRegistrarDto;

public interface TabelaPrecoService {
	TabelaPreco save(TabelaPreco entidade) throws Exception;

	TabelaPreco findByCodigo(Long codigo) throws Exception;

	void deleteByCodigo(Long codigo);

	Page<TabelaPrecoListaDto> obterTodos(Long empresa, Integer page, String conditions) throws Exception;

	TabelaPrecoDto save(Long empresa, TabelaPrecoRegistrarDto entidade) throws Exception;

	TabelaPrecoDto obterCodigo(Long empresa, Long codigo);
}
