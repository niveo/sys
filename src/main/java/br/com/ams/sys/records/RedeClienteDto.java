package br.com.ams.sys.records;

import br.com.ams.sys.entity.Empresa;
import br.com.ams.sys.entity.RedeCliente;
import br.com.ams.sys.entity.SegmentoCliente;
import lombok.Builder;

@Builder
public record RedeClienteDto(Long codigo, String descricao) {
	public RedeCliente toRedeCliente(RedeCliente emp, Empresa empresa) {
		return emp.toBuilder().descricao(descricao.toUpperCase()).empresa(empresa).build();
	}

}
