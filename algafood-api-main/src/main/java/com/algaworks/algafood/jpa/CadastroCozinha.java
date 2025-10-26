package com.algaworks.algafood.jpa;

import com.algaworks.algafood.domain.model.Cozinha;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Component
public class CadastroCozinha {

    // Anotação responsável por injetar o EntityManager (Que Já vem com o Spring Data JPA)
    // Essa anotação deixa específica para essa ‘interface’
    @PersistenceContext
    private EntityManager manager;

    public List<Cozinha> listar() {
        // Aqui estamos apenas criando a consulta
        TypedQuery<Cozinha> query = manager.createQuery("from Cozinha", Cozinha.class);

        // com o query, é possível executar a consulta
        return query.getResultList();
    }
}
/*
 * Classe somente para exercício da JPA
 * EntityManager -> Interface da JPA: Gerencia o contexto de persistência.
 *  Responsável pela intermediação dos comandos para a tradução sql.
 *
 * TypedQuery
 *
 * */