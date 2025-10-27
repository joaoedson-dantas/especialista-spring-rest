package com.algaworks.algafood.jpa;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class AlteracaoCozinhaMain {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(AlgafoodApiApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        CozinhaRepository cozinhaRepository = context.getBean(CozinhaRepository.class);

        Cozinha cozinha = new Cozinha();

        cozinha.setId(1L);
        cozinha.setNome("Brasileira");
        cozinhaRepository.salvar(cozinha);

        System.out.printf("%d - %s\n", cozinha.getId(), cozinha.getNome());
    }
}
