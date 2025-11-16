package com.algaworks.algafood.domain.repository;


import com.algaworks.algafood.domain.model.Cozinha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Registrar que é um componente do tipo Repository
public interface CozinhaRepository extends JpaRepository<Cozinha, Long> {
    // List<Cozinha> consultarPorNome(String nome);
    // List<Cozinha> findTodasCozinhasByNome(String nome);
    List<Cozinha> findTodasCozinhaByNomeContaining(String nome);

    boolean existsByNome(String nome);
}
