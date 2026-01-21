package com.algaworks.algafood.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

// Informamos para o Spring que não deve ser levando em conta para fim de instanciação
// de um repositório pelo Spring Data Jpa, ou seja,
// o Spring Data Jpa não deve instânciar uma implementação para essa interface, deve ignorar.
@NoRepositoryBean
public interface CustomJpaRepository<T, ID> extends JpaRepository<T, ID> {

    Optional<T> buscarPrimeiro();

}

/*
*  CustomJpaRepository<T, ID> -> Quando eu herdar essa interface é necessário especificar os parâmetros de tipo.
*  Para informar o tipo da Entidade e o tipo do ID da entidade
*  extends JpaRepository<T, ID> -> Após isso, repasamos o que estamos recebendo via generic para o interface repository
* */