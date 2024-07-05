package br.com.ams.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.entity.Cidade;

@Transactional(readOnly = true)
public interface CidadeRepository extends JpaRepository<Cidade, Long> {

}
