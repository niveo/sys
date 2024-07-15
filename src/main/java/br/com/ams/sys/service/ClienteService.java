package br.com.ams.sys.service;

import org.springframework.data.domain.Page;

import br.com.ams.sys.entity.Cliente;
import br.com.ams.sys.records.ClienteDto;
import br.com.ams.sys.records.ClienteListaDto;
import br.com.ams.sys.records.ClienteRegistrarDto;

public interface ClienteService {
	Cliente save(Cliente entidade) throws Exception;

	ClienteDto save(Long empresa, ClienteRegistrarDto entidade) throws Exception;

	Cliente findByCodigo(Long codigo) throws Exception;

	void deleteByCodigo(Long codigo);

	ClienteDto obterCodigo(Long empresa, Long codigo) throws Exception;

	Page<ClienteListaDto> obterTodos(Long empresa, Integer page, String conditions) throws Exception;

}
