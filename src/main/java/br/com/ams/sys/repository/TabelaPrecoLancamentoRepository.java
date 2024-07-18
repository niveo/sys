package br.com.ams.sys.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.entity.ProdutoUnidade;
import br.com.ams.sys.entity.TabelaPrecoLancamento;

@Transactional(readOnly = true)
public interface TabelaPrecoLancamentoRepository extends JpaRepository<TabelaPrecoLancamento, Long> {
	@Query("Select c From TabelaPrecoLancamento c where c.tabela.codigo = :codigo")
	Page<TabelaPrecoLancamento> findByProduto(@Param("codigo") Long codigo, Pageable pageable);
}
