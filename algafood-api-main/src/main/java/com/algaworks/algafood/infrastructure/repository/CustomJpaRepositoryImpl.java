package com.algaworks.algafood.infrastructure.repository;

import com.algaworks.algafood.domain.repository.CustomJpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.util.Optional;

public class CustomJpaRepositoryImpl<T, D> extends SimpleJpaRepository<T, D> implements CustomJpaRepository<T, D> {

    private EntityManager entityManager;

    public CustomJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @Override
    public Optional<T> buscarPrimeiro() {
        var jpql = "from " + getDomainClass().getName();
        T entity = entityManager
                .createQuery(jpql, getDomainClass()) // Para pegar a entidade TIPADA (T)
                .setMaxResults(1) // vai gerar uma consulta SQL usando o LIMIT de apenas uma linha
                .getSingleResult();

        // ofNullable - Retornando um optional (pode ter um valor nulo dentro desse retorno)
        return Optional.ofNullable(entity);
    }
}

// getDomainClass().getName() => Pegando o nome da classe para qual esse repositório exige = T