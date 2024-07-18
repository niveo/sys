package br.com.ams.sys.service;

import java.util.List;

import org.springframework.data.domain.Page;

import br.com.ams.sys.entity.ClienteEndereco;
import br.com.ams.sys.records.ClienteEnderecoDto;

public interface ClienteEnderecoService {
	Page<ClienteEnderecoDto> findByCliente(Integer page,Long codigo);

	ClienteEndereco save(ClienteEndereco registro) throws Exception;

	ClienteEnderecoDto save(ClienteEnderecoDto registro) throws Exception;

	void deleteByCodigo(Long codigo);
}
