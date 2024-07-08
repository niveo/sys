package br.com.ams.sys.entity;



import java.util.List;

import br.com.ams.sys.enuns.TipoPessoa;
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


@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
@Builder
@ToString(of = { "codigo" })
@Entity
@Table(indexes = { @Index(columnList = "codigo, empresa", unique = true) })
public class Segmento extends AbstractTimesTampEntity {

	
	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long codigo;

	@Column(nullable = false)
	private String descricao;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Empresa.class, optional = false)
	@JoinColumn(nullable = false, name = "empresa")
	private Empresa empresa;
}
