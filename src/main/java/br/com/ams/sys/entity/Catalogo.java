package br.com.ams.sys.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"codigo"})
@Entity
@Table
public class Catalogo extends AbstractTimesTampEntity {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CODIGO")
    private Integer codigo;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Empresa.class, optional = false)
    @JoinColumn
    private Empresa empresa;

    @Column(name = "ATIVO")
    private Boolean ativo;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    @OneToMany(cascade = {CascadeType.PERSIST,
            CascadeType.MERGE}, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "catalogo", targetEntity = CatalogoPagina.class)
    private List<CatalogoPagina> catalogoPaginas;
}
