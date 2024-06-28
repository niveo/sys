package br.com.ams.sys.service;

import br.com.ams.sys.entity.Empresa;

public interface EmpresaService {
	Empresa salvar(Empresa entidade) throws Exception;

	Empresa obterCodigo(Long codigo) throws Exception;

	void remover(Long codigo) throws Exception;
}
