package br.com.ams.sys.service;

import java.util.List;

import br.com.ams.sys.entity.Estado;
import br.com.ams.sys.records.EstadoDto;

public interface EstadoService {
	Estado save(Estado entidade) throws Exception;

	Estado findByCodigo(Long codigo) throws Exception;

	void deleteByCodigo(Long codigo) throws Exception;
 

	List<EstadoDto> obterTodos() throws Exception;
}
