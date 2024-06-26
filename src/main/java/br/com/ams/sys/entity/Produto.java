package br.com.ams.sys.entity;

import java.io.Serial;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = { "codigo" })
@Entity
@Table(indexes = { @Index(columnList = "codigo, empresa", unique = true) })
public class Produto extends AbstractTimesTampEntity {

	@Serial
	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long codigo;

	@Column(nullable = false)
	private String descricao;

	@Column(nullable = false)
	private String referencia;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Empresa.class, optional = false)
	@JoinColumn(nullable = false)
	private Empresa empresa;
}
