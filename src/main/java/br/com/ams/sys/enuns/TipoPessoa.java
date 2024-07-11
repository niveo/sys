package br.com.ams.sys.enuns;

import br.com.ams.sys.common.CnpjGroup;
import br.com.ams.sys.common.CpfGroup;
import lombok.Getter;

@Getter
public enum TipoPessoa {
	JURIDICA("Jurídica", "CNPJ", "00.000.000/0000-00", CnpjGroup.class),
	FISICA("Física", "CPF", "000.000.000-00", CpfGroup.class);

	private final String descricao;
	private final String documento;
	private final String mascara;
	private final Class<?> group;

	private TipoPessoa(String descricao, String documento, String mascara, Class<?> group) {
		this.descricao = descricao;
		this.documento = documento;
		this.mascara = mascara;
		this.group = group;
	}

}
