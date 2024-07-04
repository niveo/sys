package br.com.ams.sys.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.ams.sys.entity.Cliente;
import br.com.ams.sys.records.ClienteDto;
import br.com.ams.sys.records.ClienteListaDto;

public interface ClienteService {
	Cliente save(Cliente entidade) throws Exception;

	Cliente findByCodigo(Long codigo) throws Exception;

	void deleteByCodigo(Long codigo) throws Exception;

	Page<ClienteListaDto> obterTodos(Pageable pageable) throws Exception;

	ClienteDto obterCodigo(Long codigo) throws Exception;

}
