package br.com.ams.sys.entity;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = { "usuarios", "contatos", "enderecos", "empresa", "segmento", "tabela" })
@Table(indexes = { @Index(columnList = "codigo, empresa", unique = true), @Index(columnList = "cep"),
		@Index(columnList = "codigoExterno") })
public class Cliente extends AbstractClienteEmpresa {

	private static final long serialVersionUID = 1L;

	// Unidirectional
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Empresa.class, optional = false)
	@JoinColumn(nullable = false, name = "empresa")
	private Empresa empresa;

	@Column
	private String codigoExterno;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = SegmentoCliente.class, optional = true)
	@JoinColumn(name = "segmento", nullable = true)
	private SegmentoCliente segmento;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RedeCliente.class, optional = true)
	@JoinColumn(name = "rede", nullable = true)
	private RedeCliente rede;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TabelaPreco.class, optional = true)
	@JoinColumn(name = "tabela", nullable = true)
	private TabelaPreco tabela;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "clientes", targetEntity = Usuario.class)
	private List<Usuario> usuarios;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente", cascade = CascadeType.ALL, targetEntity = ClienteContato.class, orphanRemoval = true)
	private List<ClienteContato> contatos;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente", cascade = CascadeType.ALL, targetEntity = ClienteEndereco.class, orphanRemoval = true)
	private List<ClienteEndereco> enderecos;

}