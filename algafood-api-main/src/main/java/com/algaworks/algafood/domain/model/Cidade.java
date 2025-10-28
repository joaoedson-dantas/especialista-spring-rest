package com.algaworks.algafood.domain.model;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Cidade {

    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne // Uma cidade só pode ter um Estado, mas um Estado pode ter muitas cidades.
    @JoinColumn(nullable = false)
    private Estado estado;
}
