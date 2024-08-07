package br.com.ams.sys.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.entity.ClienteEndereco;

@Transactional(readOnly = true)
public interface ClienteEnderecoRepository extends JpaRepository<ClienteEndereco, Long> {
	@Query("Select c From ClienteEndereco c where c.cliente.codigo = :codigo")
	Page<ClienteEndereco> findByCliente(@Param("codigo") Long codigo, Pageable pageable);
}
