package br.com.ams.sys.entity;

import java.io.Serial;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = { "codigo" })
@Entity
@Table
public class Usuario extends AbstractTimesTampEntity {

	@Serial
	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long codigo;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String senha;

	@ManyToMany
	@JoinTable(name = "usuario_has_empresas", joinColumns = @JoinColumn(name = "usuario"), inverseJoinColumns = @JoinColumn(name = "empresa"), uniqueConstraints = {
			@UniqueConstraint(columnNames = { "usuario", "empresa" }) })
	private List<Empresa> empresas;

	@ManyToMany
	@JoinTable(name = "usuario_has_clientes", joinColumns = @JoinColumn(name = "usuario"), inverseJoinColumns = @JoinColumn(name = "cliente"), uniqueConstraints = {
			@UniqueConstraint(columnNames = { "usuario", "cliente" }) })
	private List<Cliente> clientes;

}