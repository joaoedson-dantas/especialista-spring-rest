package com.algaworks.algafood.infrastructure.repository.spec;

import com.algaworks.algafood.domain.model.Restaurante;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class RestauranteSpecs {

    public static Specification<Restaurante> comFreteGratis() {
        /*
         * A interface Specification define apenas um método abstrato: toPredicate.
         *
         * Esse método recebe três parâmetros:
         * - Root<T>
         * - CriteriaQuery<?>
         * - CriteriaBuilder
         *
         * Como Specification é uma interface funcional,
         * ela pode ser implementada utilizando expressão lambda.
         *
         * Portanto, a lambda abaixo representa a implementação
         * do método toPredicate.
         */
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("taxaFrete"), BigDecimal.ZERO);

    }

    public static Specification<Restaurante> comNomeSemelhante(String nome) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("nome"), "%" + nome + "%");
    }
}
