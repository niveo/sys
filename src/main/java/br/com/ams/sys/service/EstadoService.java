package br.com.ams.sys.service;

import br.com.ams.sys.entity.Estado;

public interface EstadoService {
	Estado salvar(Estado entidade) throws Exception;

	Estado obterCodigo(Long codigo) throws Exception;

	void remover(Long codigo) throws Exception;
}
