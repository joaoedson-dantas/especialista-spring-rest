package com.algaworks.algafood.jpa;

import com.algaworks.algafood.domain.model.Cozinha;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

// @Component
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

    public Cozinha buscar(Long id) {
        return manager.find(Cozinha.class, id);
    }

    @Transactional
    public Cozinha salvar(Cozinha cozinha) {
        // merge (Significa "fundir", ou seja, colocar a entidade dentro do contexto de persistência)
        return manager.merge(cozinha); // Vai retornar a instância do objeto persistido
    }

    @Transactional
    public void remover(Cozinha cozinha) { // 1 - Estado de transient
        // 2 -> Passando para o estado de managed, objeto gerenciado pela JPA
        cozinha = buscar(cozinha.getId());
        // 3 -> Passando para o estado de removed
        manager.remove(cozinha);
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