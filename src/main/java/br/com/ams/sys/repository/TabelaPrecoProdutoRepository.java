package br.com.ams.sys.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.entity.TabelaPrecoProduto;

@Transactional(readOnly = true)
public interface TabelaPrecoProdutoRepository extends JpaRepository<TabelaPrecoProduto, Long> {
	@Query("Select c From TabelaPrecoProduto c where c.lancamento.codigo = :codigo")
	Page<TabelaPrecoProduto> findByLancamento(@Param("codigo") Long codigo, Pageable pageable);
}
