package com.algaworks.algafood.di.service;

import com.algaworks.algafood.di.modelo.Cliente;
import com.algaworks.algafood.di.notificacao.NivelUrgencia;
import com.algaworks.algafood.di.notificacao.Notificador;
import com.algaworks.algafood.di.notificacao.TipoDoNotificador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@Component
public class AtivacaoClienteService {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private Notificador notificador;

    // Injetando um NotificadorEmail via construtor: Que vai receber como parâmetro um objeto a qual o tipo
    // seja gerenciado pelo spring;
//    @Autowired // Informa para o Spring que ele deve usar esse ponto de Injeção.
//    public AtivacaoClienteService(Notificador notificador) {
//        this.notificador = notificador;
//    }


//    public AtivacaoClienteService(String qualquer) {
//
//    }

    public void ativar(Cliente cliente) {
        cliente.ativar();

        if (notificador != null) {
            // Utilizando o padrão observer
            // Aqui vamos apenas dizer para o container que o cliente está ativo neste momento.
            // notificador.notificar(cliente, "Seu cadastro no sistema está ativo!");
            applicationEventPublisher.publishEvent(new ClienteAtivadoEvent(cliente));
        } else {
            System.out.println("Não existe notificador, mas cliente foi ativado");
        }
    }

    // @PostConstruct // Ele vai executar esse método após ser chamado o construtor e após realizar todas as injeções
    public void init() {
        System.out.println("INIT" + notificador);
    }

    // @PreDestroy // É chamado quando o bean deixa de existir, ele é chamado um pouco antes.
    public void destroy() {
        System.out.println("DESTROY");
    }
}

