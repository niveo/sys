package br.com.ams.sys.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.entity.Usuario;

@Transactional(readOnly = true)
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	//@Cacheable(value = RedisConfig.CACHE_USUARIO_EMAIL_KEY)
	Optional<Usuario> findByEmail(String email);
}
