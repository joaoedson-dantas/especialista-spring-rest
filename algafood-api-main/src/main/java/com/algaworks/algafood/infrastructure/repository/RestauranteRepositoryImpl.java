package com.algaworks.algafood.infrastructure.repository;

// Classe de customizada do Repository

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepositoryQueries;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.HashMap;
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
                .setParameter("taxaFreteFinal", taxaFreteFinal)
                .getResultList(); // Busca o resultado.
    }

    @Override
    public List<Restaurante> findDinamico(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("from Restaurante where 1 = 1 ");

        var parametros = new HashMap<String, Object>();

        if (StringUtils.hasLength(nome)) {
            jpql.append(" and nome like :nome");
            parametros.put("nome", "%" + nome + "%");
        }

        if (taxaFreteInicial != null) {
            jpql.append(" and taxaFrete >= :taxaFreteInicial");
            parametros.put("taxaFreteInicial", taxaFreteInicial);
        }

        if (taxaFreteFinal != null) {
            jpql.append(" and taxaFrete <= :taxaFreteFinal");
            parametros.put("taxaFreteFinal", taxaFreteFinal);
        }

        // Ele cria uma instância de uma consulta tipada
        TypedQuery<Restaurante> query = manager
                .createQuery(jpql.toString(), Restaurante.class);

        parametros.forEach((chave, valor) -> query.setParameter(chave, valor));
        // parametros.forEach(query::setParameter);

        return query.getResultList();
    }
}

/*
*  hasLength -> Verifica se não está nullo e se não está vazio.
* */