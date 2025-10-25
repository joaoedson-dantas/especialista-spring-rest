package com.algaworks.algafood.domain.model;

// javax.persistence -> Vem da especificação da JPA
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

// Essa classe cozinha vai representar uma tabela no banco de dados chamada cozinha
@Entity // Representa uma entidade e uma tabela (Por padrão o nome da tabela é o nome da classe)
@Table(name = "tab_cozinhas")
public class Cozinha {

    @Id // Informa que esse atributo vai representar o identificador da entidade (chave primária)
    private Long id;

    @Column(name = "nom_cozinha", length = 30)
    private String nome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cozinha cozinha = (Cozinha) o;
        return Objects.equals(id, cozinha.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
