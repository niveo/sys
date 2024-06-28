package br.com.ams.sys.entity;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Endereco implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String logradouro;

	@Column(nullable = false)
	private String numero;

	@Column(nullable = false)
	private String cep;

	@Column
	private String complemento;

	// Unidirectional
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Cidade.class, optional = false)
	@JoinColumn(nullable = false, name = "cidade")
	private Cidade cidade;

	// Unidirectional
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Bairro.class, optional = false)
	@JoinColumn(nullable = false, name = "bairro")
	private Bairro bairro;
}
