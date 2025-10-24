package com.algaworks.algafood.di;

import com.algaworks.algafood.di.notificacao.NotificadorEmail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificacaoConfig {
    /*
     *  @Bean --> Definição de bean
     *  Indica que esse método ele instância, configura e inicializa um novo objeto que será gerenciado pelo container do Spring
     * */
    @Bean
    public NotificadorEmail notificadorEmail() {
        // Configurando o bean
        NotificadorEmail notificadorEmail = new NotificadorEmail("smtp.algamail.com.br");
        notificadorEmail.setCaixaAlta(true);

        return notificadorEmail;
    }
}
