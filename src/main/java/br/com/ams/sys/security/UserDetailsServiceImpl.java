package br.com.ams.sys.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.ams.sys.entity.UserDetailsImpl;
import br.com.ams.sys.repository.UsuarioRepository;

//@Service
public class UserDetailsServiceImpl /*implements UserDetailsService*/ {

	/*@Autowired
	private UsuarioRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = userRepository.findByEmail(username)
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
		return new UserDetailsImpl(user);
	}*/

}