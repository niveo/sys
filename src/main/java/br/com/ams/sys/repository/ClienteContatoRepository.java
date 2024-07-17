package br.com.ams.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.entity.ClienteContato;

@Transactional(readOnly = true)
public interface ClienteContatoRepository extends JpaRepository<ClienteContato, Long> {
	@Query("Select c From ClienteContato c where c.cliente.codigo = :codigo")
	List<ClienteContato> findByCliente(@Param("codigo") Long codigo);
}
