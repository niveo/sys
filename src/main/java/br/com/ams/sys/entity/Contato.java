package br.com.ams.sys.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contato implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private String nome;
	private String cargo;
	private Set<String> telefones;
	private Set<String> emails;
}
