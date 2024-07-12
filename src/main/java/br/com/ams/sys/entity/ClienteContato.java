package br.com.ams.sys.entity;

import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.com.ams.sys.records.ClienteContatoDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = { "codigo" })
@Entity
public class ClienteContato extends AbstractTimesTampEntity {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long codigo;

	private String nome;
	private String cargo;
	private String observacao;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column
	private Set<String> telefones;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column
	private Set<String> emails;

	@ManyToOne(targetEntity = Cliente.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "cliente", nullable = false)
	private Cliente cliente;

	public ClienteContatoDto toClienteContatoDto() {
		return ClienteContatoDto.builder().cargo(cargo).cliente(cliente.getCodigo()).emails(emails).nome(nome)
				.observacao(observacao).telefones(telefones).codigo(codigo).build();
	}
}
