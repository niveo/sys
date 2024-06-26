package br.com.ams.sys.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = { "codigo" })
@Entity
@Table
public class CatalogoPagina implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long codigo;

    @Column
    private Integer pagina;

    @JsonManagedReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(cascade = { CascadeType.PERSIST,
            CascadeType.MERGE }, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "catalogoPagina", targetEntity = CatalogoPaginaMapeamento.class)
    private List<CatalogoPaginaMapeamento> mapeamentos;
}
