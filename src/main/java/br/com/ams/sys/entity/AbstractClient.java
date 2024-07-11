package br.com.ams.sys.entity;

import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.group.GroupSequenceProvider;

import br.com.ams.sys.common.ClienteGroupSequenceProvider;
import br.com.ams.sys.common.CnpjGroup;
import br.com.ams.sys.common.CpfGroup;
import br.com.ams.sys.enuns.TipoPessoa;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@ToString(of = { "codigo" })
@Data
@SuperBuilder(toBuilder = true)
@MappedSuperclass
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@GroupSequenceProvider(ClienteGroupSequenceProvider.class)
public abstract class AbstractClient extends AbstractTimesTampEntity {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long codigo;

	@NotBlank(message = "CPF/CNPJ é obrigatório")
	@CNPJ(groups = CnpjGroup.class, message = "CNPJ inválido")
	@CPF(groups = CpfGroup.class, message = "CPF inválido")
	@Column(unique = true, nullable = false)
	private String documento;

	@NotBlank(message = "Nome é obrigatório")
	@Column(nullable = false)
	private String nome;

	@NotBlank(message = "Razão Social é obrigatório")
	@Column(nullable = false)
	private String razaoSocial;

	@Column
	private String observacao;

	@Column
	private String telefone;

	@Email(message = "E-mail inválido")
	@Column
	private String email;

	@Column
	private String inscricaoEstadual;

	@NotNull(message = "Tipo pessoa é obrigatório")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoPessoa tipoPessoa = TipoPessoa.JURIDICA;

	@Embedded
	private Endereco endereco;

}