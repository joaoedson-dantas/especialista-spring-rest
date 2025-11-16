package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service // Informa que vai ser um componente Spring - Semântica
public class CadastroCozinhaService {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    // Nomes dos métodos -> Colocar nomes que as pessoas usam no negócio.
    public Cozinha salvar(Cozinha cozinha) {
        // Aqui colocaria outras regras de negócio.
        // ex: Só podera cadastrar uma nova cozinha de segunda a sexta.
        return cozinhaRepository.save(cozinha);
    }

    public void excluir(Long cozinhaId) {
        try {
            if (!cozinhaRepository.existsById(cozinhaId)) {
                throw new EntidadeNaoEncontradaException(
                        String.format("Não existe um cadastro de cozinha com código %d", cozinhaId));
            }

            cozinhaRepository.deleteById(cozinhaId);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format("Cozinha de código %d não pode ser removida, pois está em uso", cozinhaId));
        }
    }
}

// Sobre o EmptyResultDataAccessException

/*

*Devido a atualização de versão, o JPA não lança mais EmptyResultDataAccessException quando o id buscado não existe no momento realizar a deleção com o deleteById(),
* ele simplesmente não deleta e não avisa sobre o fato de não existir, por tanto, para que possamos manter o mesmo comportamento demonstrado na aula,
* vamos alterar o método excluir() para o seguinte:
* */