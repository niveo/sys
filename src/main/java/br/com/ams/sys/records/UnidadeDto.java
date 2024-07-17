package br.com.ams.sys.records;

import br.com.ams.sys.entity.Empresa;
import br.com.ams.sys.entity.RedeCliente;
import br.com.ams.sys.entity.SegmentoCliente;
import br.com.ams.sys.entity.Unidade;
import lombok.Builder;

@Builder
public record UnidadeDto(Long codigo, String descricao, String sigla) {
	public Unidade toUnidade(Unidade emp, Empresa empresa) {
		return emp.toBuilder().descricao(descricao.toUpperCase()).sigla(sigla.toUpperCase()).empresa(empresa).build();
	}

}
