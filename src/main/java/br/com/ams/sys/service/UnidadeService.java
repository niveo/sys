package br.com.ams.sys.service;

import org.springframework.data.domain.Page;

import br.com.ams.sys.entity.Unidade;
import br.com.ams.sys.records.UnidadeDto;

public interface UnidadeService {
	Unidade save(Unidade entidade) throws Exception;

	Unidade findByCodigo(Long codigo) throws Exception;

	void deleteByCodigo(Long codigo);

	Page<UnidadeDto> obterTodos(Long empresa, Integer page, String conditions) throws Exception;

	UnidadeDto save(Long empresa, UnidadeDto entidade) throws Exception;

	UnidadeDto obterCodigo(Long empresa, Long codigo);
}
