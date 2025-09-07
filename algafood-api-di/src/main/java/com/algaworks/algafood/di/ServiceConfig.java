package com.algaworks.algafood.di;

import com.algaworks.algafood.di.notificacao.NotificadorEmail;
import com.algaworks.algafood.di.service.AtivacaoClienteService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration // Também é um componente do Spring, mas tem o objetivo de definir beans
public class ServiceConfig {
    //@Bean
    public AtivacaoClienteService ativacaoClienteService(NotificadorEmail notificadorEmail) {
        // Aqui vai retornar um Bean já gerenciado pelo spring, pois o método chamado está anotado com @Bean

        // TODO Como faço para instânciar um Bean que depende de outros beans que não foram definidos na mesma classe
        // de configuração?

        // Deve receber essa dependência via parâmetro do método. O spring vai conseguir injetar essa dependência
        // caso tenha algum bean desse tipo no seu container.
        return new AtivacaoClienteService(notificadorEmail);
    }
}
