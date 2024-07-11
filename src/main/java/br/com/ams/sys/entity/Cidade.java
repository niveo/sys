package br.com.ams.sys.entity;

import br.com.ams.sys.records.CidadeDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "descricao", "estado" }) }, indexes = {
		@Index(columnList = "descricao"), @Index(columnList = "estado, descricao") })
public class Cidade extends AbstractTimesTampEntity {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long codigo;

	@Column(nullable = false)
	private String descricao;

	// Unidirectional
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Estado.class, optional = false)
	@JoinColumn(nullable = false, name = "estado")
	private Estado estado;

	public CidadeDto toDto() {
		return CidadeDto.builder().codigo(codigo).descricao(descricao).estado(estado.toDto()).build();
	}
}
