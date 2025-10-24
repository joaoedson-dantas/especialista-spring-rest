package com.algaworks.algafood.di;

import com.algaworks.algafood.di.notificacao.NotificadorEmail;
import com.algaworks.algafood.di.service.AtivacaoClienteService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Classe de configuração
// @Configuration // Aqui é necessário mostrar para o spring como ele deve instânciar e configurar essa classe.
// Também é um componente do Spring, mas tem o objetivo de definir beans
public class AlgaConfig {

    /*
    *  @Bean --> Definição de bean
    *  Indica que esse método ele instância, configura e inicializa um novo objeto que será gerenciado pelo container do Spring
    * */
//    @Bean
//    public NotificadorEmail notificadorEmail() {
//        // Configurando o bean
//        NotificadorEmail notificadorEmail = new NotificadorEmail("smtp.algamail.com.br");
//        notificadorEmail.setCaixaAlta(true);
//
//        return notificadorEmail;
//    }
//
//    @Bean
//    public AtivacaoClienteService ativacaoClienteService() {
//        // Aqui vai retornar um Bean já gerenciado pelo spring, pois o método chamado está anotado com @Bean
//        return new AtivacaoClienteService(this.notificadorEmail());
//    }

}
