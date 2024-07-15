package br.com.ams.sys.records;

import br.com.ams.sys.entity.Empresa;
import br.com.ams.sys.entity.SegmentoCliente;
import lombok.Builder;

@Builder
public record SegmentoClienteDto(Long codigo, String descricao) {
	public SegmentoCliente toSegmentoCliente(SegmentoCliente emp, Empresa empresa) {
		return emp.toBuilder().descricao(descricao.toUpperCase()).empresa(empresa).build();
	}

}
