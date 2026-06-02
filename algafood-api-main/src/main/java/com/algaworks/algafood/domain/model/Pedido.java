package com.algaworks.algafood.domain.model;


import com.algaworks.algafood.domain.model.enums.StatusPedido;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Pedido {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Id
    private Long id;

    @Column(nullable = false)
    private BigDecimal subtotal;

    @Column(nullable = false)
    private BigDecimal taxaFrente;

    @Column(nullable = false)
    private BigDecimal valorTotal;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    private LocalDateTime dataConfirmacao;
    private LocalDateTime dataCancelamento;
    private LocalDateTime dataEntrega;

    @Embedded
    private Endereco enderecoEntrega;

    private StatusPedido status;

    // Muitos pedidos possuem apenas um restaurante
    @ManyToOne // Um pedido só pode ter um restaurante, mas um restaurante pode ter vários pedidos.
    @JoinColumn(nullable = false)
    private Restaurante restaurante;

    // Muitos pedidos possuem apenas uma forma de pagamento.
    @ManyToOne // Um pedido só pode ter uma forma de pagamento, mas uma forma de pagamento pode ter vários pedidos
    @JoinColumn(nullable = false)
    private FormaPagamento formaPagamento;

    // Muitos pedidos possuem apenas um cliente - Não tem como um pedido ter mais de um cliente - Regra.
    @ManyToOne
    @JoinColumn(name = "usuario_cliente_id", nullable = false)
    private Usuario cliente;

    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> itens = new ArrayList<>();
}
