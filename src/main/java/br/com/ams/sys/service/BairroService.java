package br.com.ams.sys.service;

import br.com.ams.sys.entity.Bairro;

public interface BairroService {
	Bairro salvar(Bairro entidade) throws Exception;

	Bairro obterCodigo(Long codigo) throws Exception;

	void remover(Long codigo) throws Exception;
}
