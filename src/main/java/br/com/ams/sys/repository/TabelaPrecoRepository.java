package br.com.ams.sys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.entity.TabelaPreco;

@Transactional(readOnly = true)
public interface TabelaPrecoRepository extends JpaRepository<TabelaPreco, Long> {

}
