package br.com.ams.sys.service;

import java.util.List;

import org.springframework.data.domain.Page;

import br.com.ams.sys.entity.ClienteContato;
import br.com.ams.sys.records.ClienteContatoDto;

public interface ClienteContatoService {
	Page<ClienteContatoDto> findByCliente(Integer page,Long codigo);

	ClienteContato save(ClienteContato registro) throws Exception;

	ClienteContatoDto save(ClienteContatoDto registro) throws Exception;

	void deleteByCodigo(Long codigo);
}
