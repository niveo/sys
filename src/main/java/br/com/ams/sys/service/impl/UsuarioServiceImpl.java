package br.com.ams.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.entity.Usuario;
import br.com.ams.sys.repository.UsuarioRepository;
import br.com.ams.sys.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public Usuario salvar(Usuario entidade) throws Exception {
		return usuarioRepository.save(entidade);
	}

	@Override
	public Usuario obterCodigo(Long codigo) throws Exception {
		return this.usuarioRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
	}

	@Override
	public void remover(Long codigo) throws Exception {
		this.usuarioRepository.deleteById(codigo);
	}
}
