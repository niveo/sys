package br.com.ams.sys.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.ams.sys.records.TabelaPrecoProdutoDto;
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
@Table(indexes = { @Index(columnList = "lancamento, produto", unique = true) })
public class TabelaPrecoProduto implements Serializable {
	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long codigo;

	@ManyToOne
	@JoinColumn(name = "lancamento", nullable = false)
	private TabelaPrecoLancamento lancamento;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Produto.class, optional = false)
	@JoinColumn(nullable = false, name = "produto")
	private Produto produto;

	@Column(nullable = false)
	private BigDecimal valor;

	public TabelaPrecoProdutoDto toTabelaPrecoProdutoDto() {
		return TabelaPrecoProdutoDto.builder().codigo(codigo).lancamento(lancamento.getCodigo())
				.produto(produto.toProdutoListaDto()).valor(valor).build();
	}

}
