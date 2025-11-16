package com.algaworks.algafood.domain.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


@JsonRootName("cozinha") // -> Nome da raiz que será retornada no XML.
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

    // @JsonIgnore -> Remove da representação
    // @JsonProperty("titulo") -> Altera o nome na representação
    // '@Column(name = "nom_cozinha", length = 30)
    @Column(nullable = false)
    private String nome;
}
