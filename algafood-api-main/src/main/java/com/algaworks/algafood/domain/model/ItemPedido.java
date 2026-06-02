package com.algaworks.algafood.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class ItemPedido {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Id
    private Long id;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private BigDecimal precoUnitario;

    @Column(nullable = false)
    private BigDecimal precoTotal;

    // Muitos itemPedido possuem apenas um Pedido;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Pedido pedido;

    // Muitos itemPedidos possuem apenas um Produto.
    @ManyToOne
    @JoinColumn(nullable = false)
    private Produto produto;

    private String observacao;
}
