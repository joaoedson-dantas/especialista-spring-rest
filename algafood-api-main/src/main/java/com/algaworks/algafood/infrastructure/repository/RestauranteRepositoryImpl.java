package com.algaworks.algafood.infrastructure.repository;

// Classe de customizada do Repository

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepositoryQueries;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

    @PersistenceContext // Serve para fazer a injeção do contexto de persistência
    private EntityManager manager;

    // Nome do método não tem relevância, pode ser qualquer nome
    public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {

        var jpql = "from Restaurante where nome like :nome " +
                "and taxaFrete between :taxaFreteInicial and :taxaFreteFinal";

        return manager.createQuery(jpql, Restaurante.class) // Aqui vai criar a query, passando a consulta em JPQL e o seu tipo de retorno
                .setParameter("nome", "%" + nome + "%") // adicionando os parâmetros da consulta
                .setParameter("taxaFreteInicial", taxaFreteInicial)
                .setParameter("taxaFreteFinal" ,taxaFreteFinal)
                .getResultList(); // Busca o resultado.
    }
}
