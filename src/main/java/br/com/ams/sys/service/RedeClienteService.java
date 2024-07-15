package br.com.ams.sys.service;

import org.springframework.data.domain.Page;

import br.com.ams.sys.entity.RedeCliente;
import br.com.ams.sys.records.RedeClienteDto; 

public interface RedeClienteService {
	RedeCliente save(RedeCliente entidade) throws Exception;

	RedeCliente findByCodigo(Long codigo) throws Exception;

	void deleteByCodigo(Long codigo);

	Page<RedeClienteDto> obterTodos(Long empresa, Integer page, String conditions) throws Exception;

	RedeClienteDto save(Long empresa, RedeClienteDto entidade) throws Exception;

	RedeClienteDto obterCodigo(Long empresa, Long codigo);
}
