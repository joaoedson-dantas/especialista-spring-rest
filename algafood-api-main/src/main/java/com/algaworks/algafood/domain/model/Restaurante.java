package com.algaworks.algafood.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @JsonIgnore
    @Embedded // Indica que essa propriedade é uma classe do tipo incorporado. Ou seja, é uma parte da entidade Restaurante.
    private Endereco endereco;

    @JsonIgnore
    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime dataCadastro; // LocalDateTime: Representa uma data e hora sem fuso horário, sem timestamp

    @JsonIgnore
    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime dataAtualizacao;

    /*
    *  Criando relacionamento @ManyToMany: Muitos restaurantes possuem muitas formas de pagamentos
    *  Para isso funcionar, precisamos criar uma tabela pivô, será intermediária `restaurante_forma_pagamento` vai existir
    *  para ser possível criar essa relação de muitos para muitos.
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
