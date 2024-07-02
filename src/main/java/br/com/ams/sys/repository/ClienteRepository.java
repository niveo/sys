package br.com.ams.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.entity.Cliente;
import br.com.ams.sys.records.ClienteDto;

@Transactional(readOnly = true)
public interface ClienteRepository extends JpaRepository<Cliente, Long> { 
}
