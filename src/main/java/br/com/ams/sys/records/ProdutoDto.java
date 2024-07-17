package br.com.ams.sys.records;

import br.com.ams.sys.entity.Empresa;
import br.com.ams.sys.entity.Produto;
import lombok.Builder;

@Builder
public record ProdutoDto(Long codigo, String descricao, String referencia, Boolean ativo, UnidadeDto unidade) {
	public Produto toProduto(Produto emp, Empresa empresa) {
		return emp.toBuilder().descricao(descricao.toUpperCase()).referencia(referencia.toUpperCase()).empresa(empresa)
				.ativo(ativo).build();
	}

}
