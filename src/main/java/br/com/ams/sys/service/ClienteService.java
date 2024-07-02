package br.com.ams.sys.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.ams.sys.entity.Cliente;
import br.com.ams.sys.records.ClienteDto;

public interface ClienteService {
	Cliente salvar(Cliente entidade) throws Exception;

	Cliente obterCodigo(Long codigo) throws Exception;

	Page<ClienteDto> obterTodos(Pageable pageable) throws Exception;

	void remover(Long codigo) throws Exception;
}
