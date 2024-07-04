
package br.com.ams.sys.service;

import br.com.ams.sys.entity.Usuario;
import br.com.ams.sys.records.LoginRequest;
import br.com.ams.sys.records.LoginResponse;
import br.com.ams.sys.records.UsuarioCriarDto;

public interface UsuarioService {
	Usuario save(Usuario entidade) throws Exception;

	void criar(UsuarioCriarDto entidade);

	Usuario findByCodigo(Long codigo) throws Exception;

	void deleteByCodigo(Long codigo) throws Exception;

	LoginResponse autenticar(LoginRequest record);
}
