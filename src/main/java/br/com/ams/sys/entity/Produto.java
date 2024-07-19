package br.com.ams.sys.entity;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.ams.sys.records.ProdutoListaDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(indexes = { @Index(columnList = "codigo, empresa", unique = true), @Index(columnList = "codigoExterno") })
public class Produto extends BaseEntityEmpresaExterno {

	private static final long serialVersionUID = 1L;

	@NotBlank
	@Column(nullable = false)
	private String descricao;

	@Column
	private String complemento;

	@NotBlank
	@Column(nullable = false)
	private String referencia;

	@Column(nullable = false)
	private Boolean ativo = false;

	@Column
	private String observacao;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Unidade.class, optional = true)
	@JoinColumn(name = "unidade")
	private Unidade unidade;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "produto", cascade = CascadeType.ALL, targetEntity = ProdutoUnidade.class, orphanRemoval = true)
	private List<ProdutoUnidade> unidades;

	public ProdutoListaDto toProdutoListaDto() { 
		return ProdutoListaDto.builder().ativo(ativo).codigo(getCodigo()).descricao(descricao).referencia(referencia)
				.build();
	}
}
