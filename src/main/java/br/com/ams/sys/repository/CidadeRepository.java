package br.com.ams.sys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.entity.Cidade;

@Transactional(readOnly = true)
public interface CidadeRepository extends JpaRepository<Cidade, Long> {

}
