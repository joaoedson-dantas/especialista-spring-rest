package com.algaworks.algafood.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Entity
public class Restaurante {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private BigDecimal taxaFrete;

    @JoinColumn(nullable = false)
    @ManyToOne // Muitos Restaurantes possui uma Cozinha
    // @JoinColumn(name = "cozinha_id") por default já vem com esse nome
    private Cozinha cozinha;

    @Embedded // Indica que essa propriedade é uma classe do tipo incorporado. Ou seja, é uma parte da entidade Restaurante.
    private Endereco endereco;

    /*
    *  Criando relacionamento @ManyToMany
    * */
    @JsonIgnore
    @ManyToMany // Muitos restaurantes possuem muitas formas de pagamento.
    @JoinTable( // Ajuda a costumizar como ficará o nome da tabela intermediaria, assim como as colunas
            name = "restaurante_forma_pagamento", // nome da tabela
            joinColumns = { // Vai definir qual o nome da coluna, da tabela intermediária, que associa a restaurante.
                @JoinColumn(name = "restaurante_id")  // O JoinColumn -> Define o nome da coluna que faz referência a própria classe que estamos mapeando, no caso restaurante
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "forma_pagamento_id")
            }
    )
    private List<FormaPagamento> formasPagamento = new ArrayList<>();
}
