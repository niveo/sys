package br.com.ams.sys.service;

import java.util.List;

import br.com.ams.sys.entity.Estado;
import br.com.ams.sys.records.EstadoDto;

public interface EstadoService {
	Estado save(Estado entidade) throws Exception;

	Estado findByCodigo(Long codigo) throws Exception;

	Estado findBySigla(String sigla) throws Exception;

	void deleteByCodigo(Long codigo);

	List<EstadoDto> obterTodos() throws Exception;
}
