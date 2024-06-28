package br.com.ams.sys.entity;

import java.io.Serial;

import br.com.ams.sys.enuns.TipoPessoa;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(of = { "codigo" })
@Data
@MappedSuperclass
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public abstract class AbstractClient extends AbstractTimesTampEntity {

	@Serial
	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long codigo;

	@Column(unique = true, nullable = false)
	private String documento;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false)
	private String razaoSocial;

	@Column
	private String observacao;
	
	@Column
	private String telefone;
	
	@Column
	private String email;
	
	@Column
	private String inscricaoEstadual;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoPessoa tipoPessoa = TipoPessoa.JURIDICA;

	@Embedded
	private Endereco endereco;

}