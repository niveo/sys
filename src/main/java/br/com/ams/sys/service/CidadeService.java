package br.com.ams.sys.service;

import br.com.ams.sys.entity.Cidade;

public interface CidadeService {
	Cidade salvar(Cidade entidade) throws Exception;

	Cidade obterCodigo(Long codigo) throws Exception;

	void remover(Long codigo) throws Exception;
}
