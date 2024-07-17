package br.com.ams.sys.entity;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
public class TabelaPreco extends BaseEntityEmpresaExterno {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String descricao;

	@Column(nullable = false)
	private Boolean ativo = false;

	@Column
	private String observacao;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@OneToMany(mappedBy = "tabela", targetEntity = TabelaPrecoLancamento.class)
	private List<TabelaPrecoLancamento> lancamentos;

}
