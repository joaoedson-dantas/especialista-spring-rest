package com.algaworks.algafood.jpa;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.*;
import com.algaworks.algafood.domain.repository.*;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.Objects;

public class InclusaoCozinhaMain {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(AlgafoodApiApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        var repository = context.getBean(CidadeRepository.class);
        var repositoryEstado = context.getBean(EstadoRepository.class);
        Estado estado = repositoryEstado.buscar(3L);

        Cidade cidade = new Cidade();
        cidade.setNome("Paramoti");
        cidade.setEstado(estado);

        cidade = repository.salvar(cidade);

        System.out.printf("%d - %s\n", cidade.getId(), cidade.getNome());
    }

}
