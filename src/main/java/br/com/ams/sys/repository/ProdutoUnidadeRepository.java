package br.com.ams.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.entity.ProdutoUnidade;

@Transactional(readOnly = true)
public interface ProdutoUnidadeRepository extends JpaRepository<ProdutoUnidade, Long> {
	@Query("Select c From ProdutoUnidade c where c.produto.codigo = :codigo")
	List<ProdutoUnidade> findByProduto(@Param("codigo") Long codigo);
}
