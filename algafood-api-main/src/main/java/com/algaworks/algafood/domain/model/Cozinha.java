package com.algaworks.algafood.domain.model;

// javax.persistence -> Vem da especificação da JPA
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
// Essa classe cozinha vai representar uma tabela no banco de dados chamada cozinha
@Entity // Representa uma entidade e uma tabela (Por padrão o nome da tabela é o nome da classe)
// @Table(name = "tab_cozinhas")
public class Cozinha {

    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Definindo a geração do valor para ID utilizando a estratégia de IDENTITY
    @Id // Informa que esse atributo vai representar o identificador da entidade (chave primária)
    private Long id;

    // '@Column(name = "nom_cozinha", length = 30)
    @Column(nullable = false)
    private String nome;
}
