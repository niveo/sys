package br.com.ams.sys.entity;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = { "codigo" })
@Entity
public class ClienteEndereco extends AbstractTimesTampEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long codigo;

	public ClienteEndereco(Cliente cliente, Endereco endereco) {
		this.cliente = cliente;
		this.endereco = endereco;
	}

	@Embedded
	private Endereco endereco;

	@Column
	private String observacao;

	@ManyToOne(targetEntity = Cliente.class)
	@JoinColumn(name = "cliente", nullable = false)
	private Cliente cliente;
}
