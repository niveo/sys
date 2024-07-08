package br.com.ams.sys.entity;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.ams.sys.enuns.TipoPessoa;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = { "codigo" })
@Entity
@Table(indexes = { @Index(columnList = "codigo, empresa", unique = true) })
public class Catalogo extends AbstractTimesTampEntity {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long codigo;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Empresa.class, optional = false)
	@JoinColumn(nullable = false, name = "empresa", insertable = false, updatable = false)
	private Empresa empresa;

	@Column(nullable = false)
	private Boolean ativo = false;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonManagedReference
	@OneToMany(cascade = { CascadeType.PERSIST,
			CascadeType.MERGE }, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "catalogo", targetEntity = CatalogoPagina.class)
	private List<CatalogoPagina> catalogoPaginas;
}
