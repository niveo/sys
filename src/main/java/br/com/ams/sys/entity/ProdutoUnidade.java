package br.com.ams.sys.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.com.ams.sys.enuns.TipoUnidadeBarra;
import br.com.ams.sys.enuns.TipoUnidadeOperacao;
import br.com.ams.sys.records.ProdutoUnidadeDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "produto", "unidade", "barra" }) })
public class ProdutoUnidade extends AbstractTimesTampEntity {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long codigo;

	@NotNull(message = "Tipo operação é obrigatório")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoUnidadeOperacao tipoOperacao;

	@NotNull
	@Column(nullable = false)
	private BigDecimal quantidade;

	@NotNull
	@Column(nullable = false)
	private BigDecimal valor;

	@NotNull(message = "Tipo barra é obrigatório")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoUnidadeBarra tipoBarra;

	@NotBlank
	private String barra;

	@ManyToOne
	@JoinColumn(name = "unidade", nullable = false)
	private Unidade unidade;

	@ManyToOne(targetEntity = Produto.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "produto", nullable = false)
	private Produto produto;

	public ProdutoUnidadeDto toProdutoUnidadeDto() {
		return ProdutoUnidadeDto.builder().codigo(codigo).barra(barra).produto(produto.getCodigo())
				.quantidade(quantidade).tipoBarra(tipoBarra).tipoOperacao(tipoOperacao)
				.unidade(unidade.toUnidadeDto())
				.valor(valor).build();
	}
}
