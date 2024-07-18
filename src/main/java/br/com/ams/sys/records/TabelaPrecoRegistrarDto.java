package br.com.ams.sys.records;

import br.com.ams.sys.entity.Empresa;
import br.com.ams.sys.entity.TabelaPreco;

public record TabelaPrecoRegistrarDto(Long codigo, String descricao, Boolean ativo, String observacao) {
	public TabelaPreco toTabelaPreco(TabelaPreco emp, Empresa empresa) {
		return emp.toBuilder().descricao(descricao.toUpperCase()).empresa(empresa).ativo(ativo).observacao(observacao)
				.build();
	}
}
