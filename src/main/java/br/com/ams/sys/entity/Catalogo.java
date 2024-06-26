package br.com.ams.sys.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serial;
import java.util.List;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = { "codigo" })
@Entity
@Table(indexes = { @Index(columnList = "codigo, empresa", unique = true) })
public class Catalogo extends AbstractTimesTampEntity {

	@Serial
	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long codigo;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Empresa.class, optional = false)
	@JoinColumn(nullable = false)
	private Empresa empresa;

	@Column(nullable = false)
	private Boolean ativo = false;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonManagedReference
	@OneToMany(cascade = { CascadeType.PERSIST,
			CascadeType.MERGE }, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "catalogo", targetEntity = CatalogoPagina.class)
	private List<CatalogoPagina> catalogoPaginas;
}
