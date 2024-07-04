package br.com.ams.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.entity.Role;
import br.com.ams.sys.entity.UserDetailsImpl;
import br.com.ams.sys.entity.Usuario;
import br.com.ams.sys.records.LoginRequest;
import br.com.ams.sys.records.LoginResponse;
import br.com.ams.sys.records.UsuarioCriarDto;
import br.com.ams.sys.repository.UsuarioRepository;
import br.com.ams.sys.service.JwtService;
import br.com.ams.sys.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtTokenService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Usuario save(Usuario request) throws Exception {
		return usuarioRepository.save(request);
	}

	@Override
	public void criar(UsuarioCriarDto request) {
		var usuario = Usuario.builder().email(request.email()).senha(passwordEncoder.encode(request.senha()))
				.roles(List.of(Role.builder().name(request.role()).build())).nome(request.email()).build();
		usuarioRepository.save(usuario);
	}

	@Override
	public Usuario findByCodigo(Long codigo) throws Exception {
		return this.usuarioRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
	}

	@Override
	public void deleteByCodigo(Long codigo) throws Exception {
		this.usuarioRepository.deleteById(codigo);
	}

	public LoginResponse autenticar(LoginRequest record) {
		// Cria um objeto de autenticação com o email e a senha do usuário
		var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(record.email(),
				record.senha());

		// Autentica o usuário com as credenciais fornecidas
		var authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		// Obtém o objeto UserDetails do usuário autenticado
		var userDetails = (UserDetailsImpl) authentication.getPrincipal();

		// Gera um token JWT para o usuário autenticado
		return new LoginResponse(jwtTokenService.generateToken(userDetails));
	}
}
