package br.com.ams.sys.service;

import java.util.List;

import br.com.ams.sys.entity.ClienteEndereco;
import br.com.ams.sys.records.ClienteEnderecoDto;

public interface ClienteEnderecoService {
	List<ClienteEnderecoDto> findByCliente(Long codigo);

	ClienteEndereco save(ClienteEndereco registro) throws Exception;

	ClienteEnderecoDto save(ClienteEnderecoDto registro) throws Exception;

	void deleteByCodigo(Long codigo);
}
