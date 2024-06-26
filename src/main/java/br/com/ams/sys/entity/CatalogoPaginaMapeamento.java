package br.com.ams.sys.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

public class CatalogoPaginaMapeamento implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long codigo;

	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn
	private CatalogoPagina catalogoPagina;

	@OneToOne(fetch = FetchType.EAGER, targetEntity = Produto.class, optional = false)
	@JoinColumn
	private Produto produto;

	@Column
	private Float inicialPosicalX;

	@Column
	private Float finalPosicalX;

	@Column
	private Float inicialPosicalY;

	@Column
	private Float finalPosicalY;

	@Column
	private Float width;

	@Column
	private Float height;
}
