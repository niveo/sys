package br.com.ams.sys.records;

import java.math.BigDecimal;

import br.com.ams.sys.entity.Produto;
import br.com.ams.sys.entity.TabelaPrecoLancamento;
import br.com.ams.sys.entity.TabelaPrecoProduto;
import lombok.Builder;

@Builder
public record TabelaPrecoProdutoRegisterDto(Long codigo, Long lancamento, Long produto, BigDecimal valor) {
	public TabelaPrecoProduto toTabelaPrecoProduto(TabelaPrecoProduto em, TabelaPrecoLancamento lancamento,
			Produto produto) {
		return em.toBuilder().codigo(codigo).lancamento(lancamento).produto(produto).valor(valor).build();
	}
}
