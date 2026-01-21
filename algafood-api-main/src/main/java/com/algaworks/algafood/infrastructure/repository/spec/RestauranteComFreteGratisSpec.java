package com.algaworks.algafood.infrastructure.repository.spec;

import com.algaworks.algafood.domain.model.Restaurante;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;


/*
*  Essa é a especificação do domínio Restaurante, é uma regra de negócio e
*  toda a vez que precisar mudar, mudaríamos somente a especificação.
*
*  Ela vai representar um filtro, onde taxaFrete é zero.
* */
public class RestauranteComFreteGratisSpec implements Specification<Restaurante> {

    /*
    *  Método responsável por criar um predicate.
    *  Predicate: Predicado é um critério, como um filtro.
    *
    *  Esse método, já recebe o root, criteriaQuery e Builder
    *
    *  root: É a origem dos dados. Pense nele como o FROM do SQL. Ele define sobre qual entidade você está pesquisando.
    *   A partir desse root, podemos pegar o atributo, que vai ser uma String.
    *
    *  CriteriaQuery - Representa a estrutura da sua consulta (o que você quer selecionar, como quer ordenar, etc.).
    *
    *  CriteriaBuilder -> É a "fábrica". Você usa-o para criar a query em si e para construir as cláusulas (filtros como equal, like, greaterThan).
     * */
    @Override
    public Predicate toPredicate(Root<Restaurante> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        // Aqui, vou ter que criar a condição da especificação, que representa um filtro onde taxaFrete é zero.
        return criteriaBuilder.equal(root.get("taxaFrete"), BigDecimal.ZERO);
    }
}
