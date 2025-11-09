package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service // Informa que vai ser um componente Spring - Semântica
public class CadastroCozinhaService {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    // Nomes dos métodos -> Colocar nomes que as pessoas usam no negócio.
    public Cozinha salvar(Cozinha cozinha) {
        // Aqui colocaria outras regras de negócio.
        // ex: Só podera cadastrar uma nova cozinha de segunda a sexta.
        return cozinhaRepository.salvar(cozinha);
    }

    public void excluir(Long id) {
        try {
            cozinhaRepository.remover(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntidadeNaoEncontradaException(
                    String.format("Não existe um cadastro de cozinha com o código %d", id)
            );

        } catch (DataIntegrityViolationException e) {
            // Como é uma exceção de infra, podemos fazer a tradução dessa exceção, lançando uma nova.
            // Nesse caso, será lançado uma exceção de negócio.
            throw new EntidadeEmUsoException(
                    String.format("Cozinha de código %d não pode ser removida, pois está em uso", id)
            );

        }
    }
}
