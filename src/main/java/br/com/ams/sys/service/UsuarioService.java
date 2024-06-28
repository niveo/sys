
package br.com.ams.sys.service;

import br.com.ams.sys.entity.Usuario;

public interface UsuarioService {
	Usuario salvar(Usuario entidade) throws Exception;

	Usuario obterCodigo(Long codigo) throws Exception;

	void remover(Long codigo) throws Exception;
}
