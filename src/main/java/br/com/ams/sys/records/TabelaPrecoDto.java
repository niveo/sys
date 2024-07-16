package br.com.ams.sys.records;

import br.com.ams.sys.entity.Empresa;
import br.com.ams.sys.entity.RedeCliente;
import br.com.ams.sys.entity.SegmentoCliente;
import br.com.ams.sys.entity.TabelaPreco;
import lombok.Builder;

@Builder
public record TabelaPrecoDto(Long codigo, String descricao) {
	public TabelaPreco toTabelaPreco(TabelaPreco emp, Empresa empresa) {
		return emp.toBuilder().descricao(descricao.toUpperCase()).empresa(empresa).build();
	}

}
