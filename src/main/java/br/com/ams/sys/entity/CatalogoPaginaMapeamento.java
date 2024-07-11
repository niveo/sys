package br.com.ams.sys.entity;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(of = { "codigo" })
@Entity
@Table
public class CatalogoPaginaMapeamento implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long codigo;

	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "catalogo_pagina")
	private CatalogoPagina catalogoPagina;

	@OneToOne(fetch = FetchType.EAGER, targetEntity = Produto.class, optional = false)
	@JoinColumn(nullable = false, name = "produto")
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
