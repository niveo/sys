package br.com.ams.sys.records;

import java.math.BigDecimal;
import java.util.Set;

import br.com.ams.sys.entity.Cliente;
import br.com.ams.sys.entity.ClienteContato;
import br.com.ams.sys.entity.Produto;
import br.com.ams.sys.entity.ProdutoUnidade;
import br.com.ams.sys.entity.Unidade;
import br.com.ams.sys.enuns.TipoUnidadeBarra;
import br.com.ams.sys.enuns.TipoUnidadeOperacao;
import lombok.Builder;

@Builder
public record ProdutoUnidadeDto(Long codigo, TipoUnidadeOperacao tipoOperacao, BigDecimal quantidade, BigDecimal valor,
		TipoUnidadeBarra tipoBarra, String barra, Long unidade, Long produto) {

	public ProdutoUnidade toProdutoUnidade(ProdutoUnidade emp, Unidade unidade, Produto produto) {
		return emp.toBuilder().barra(barra).codigo(codigo).produto(produto).quantidade(quantidade).tipoBarra(tipoBarra)
				.tipoOperacao(tipoOperacao).unidade(unidade).valor(valor).build();
	}

}
