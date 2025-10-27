package com.algaworks.algafood.domain.model;

// javax.persistence -> Vem da especificação da JPA
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;


@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
// Essa classe cozinha vai representar uma tabela no banco de dados chamada cozinha
@Entity // Representa uma entidade e uma tabela (Por padrão o nome da tabela é o nome da classe)
// @Table(name = "tab_cozinhas")
public class Cozinha {

    @EqualsAndHashCode.Include
    @Id // Informa que esse atributo vai representar o identificador da entidade (chave primária)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Definindo a geração do valor para ID utilizando a estratégia de IDENTITY
    private Long id;

    // '@Column(name = "nom_cozinha", length = 30)
    private String nome;
}
