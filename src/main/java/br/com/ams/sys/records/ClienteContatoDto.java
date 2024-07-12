package br.com.ams.sys.records;

import java.util.Set;

import br.com.ams.sys.entity.Cliente;
import br.com.ams.sys.entity.ClienteContato;
import lombok.Builder;

@Builder
public record ClienteContatoDto(Long codigo, String nome, Long cliente, String cargo, String observacao,
		Set<String> telefones, Set<String> emails) {

	public ClienteContato toClienteContato(ClienteContato emp, Cliente cliente) {
		return emp.toBuilder().cargo(cargo).cliente(cliente).codigo(codigo).emails(emails).observacao(observacao)
				.nome(nome).telefones(telefones).build();
	}

}
