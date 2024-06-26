package br.com.ams.sys.entity;

import java.io.Serial;

import br.com.ams.sys.enuns.TipoPessoa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(indexes = { @Index(columnList = "codigo, empresa", unique = true) })
public class Cliente extends AbstractTimesTampEntity {

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

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Empresa.class, optional = false)
	@JoinColumn(nullable = false)
	private Empresa empresa;
}