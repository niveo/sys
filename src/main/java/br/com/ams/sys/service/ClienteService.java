package br.com.ams.sys.service;

import br.com.ams.sys.entity.Cliente;

public interface ClienteService {
	Cliente salvar(Cliente entidade) throws Exception;

	Cliente obterCodigo(Long codigo) throws Exception;

	void remover(Long codigo) throws Exception;
}
