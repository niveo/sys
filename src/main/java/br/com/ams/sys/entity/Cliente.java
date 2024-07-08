package br.com.ams.sys.entity;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import br.com.ams.sys.enuns.TipoPessoa;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = { @Index(columnList = "codigo, empresa", unique = true) })
public class Cliente extends AbstractClient {

	private static final long serialVersionUID = 1L;

	@Column
	private String suframa;

	// Unidirectional
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Empresa.class, optional = false)
	@JoinColumn(nullable = false, name = "empresa")
	private Empresa empresa;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Segmento.class, optional = true)
	@JoinColumn(name = "segmento")
	private Segmento segmento;

	@ManyToMany(mappedBy = "clientes", targetEntity = Usuario.class)
	private List<Usuario> usuarios;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, targetEntity = ClienteContato.class, orphanRemoval = true)
	private List<ClienteContato> contatos;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, targetEntity = ClienteEndereco.class, orphanRemoval = true)
	private List<ClienteEndereco> enderecos;

}