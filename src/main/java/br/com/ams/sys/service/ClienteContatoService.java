package br.com.ams.sys.service;

import java.util.List;

import br.com.ams.sys.entity.ClienteContato;
import br.com.ams.sys.records.ClienteContatoDto;

public interface ClienteContatoService {
	List<ClienteContatoDto> findByCliente(Long cliente);

	ClienteContato save(ClienteContato registro) throws Exception;

	ClienteContatoDto save(ClienteContatoDto registro) throws Exception;

	void deleteByCodigo(Long codigo);
}
