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
    @Column(name = "CODIGO")
    private Integer codigo;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
    private CatalogoPagina catalogoPagina;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Produto.class, optional = false)
    @JoinColumn
    private Produto produto;

    @Column(name = "INICIAL_POSICAOX")
    private Float inicialPosicalX;

    @Column(name = "FINAL_POSICAOX")
    private Float finalPosicalX;

    @Column(name = "INICIAL_POSICAOY")
    private Float inicialPosicalY;

    @Column(name = "FINAL_POSICAOY")
    private Float finalPosicalY;

    @Column(name = "WIDTH")
    private Float width;

    @Column(name = "HEIGHT")
    private Float height;
}
