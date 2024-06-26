package br.com.ams.sys.entity;

import java.io.Serial;

import br.com.ams.sys.enuns.TipoPessoa;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = { "codigo" })
@Entity
@Table
public class Empresa extends AbstractTimesTampEntity {

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

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoPessoa tipoPessoa = TipoPessoa.JURIDICA;
}
