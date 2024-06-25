package br.com.ams.sys.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = { "codigo" })
@Entity
@Table
public class Produto extends AbstractTimesTampEntity {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CODIGO")
    private Integer codigo;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Empresa.class, optional = false)
    @JoinColumn
    private Empresa empresa;
}
