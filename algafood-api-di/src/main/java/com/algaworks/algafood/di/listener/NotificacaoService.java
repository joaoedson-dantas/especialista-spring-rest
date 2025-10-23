package com.algaworks.algafood.di.listener;

import com.algaworks.algafood.di.notificacao.NivelUrgencia;
import com.algaworks.algafood.di.notificacao.Notificador;
import com.algaworks.algafood.di.notificacao.TipoDoNotificador;
import com.algaworks.algafood.di.service.ClienteAtivadoEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoService {

    // @TipoDoNotificador(NivelUrgencia.NORMAL)
    @Autowired
    private Notificador notificador;

    @EventListener // Informo para o Spring que esse método é um ouvinte de um evento
    public void clienteAtivadoListener(ClienteAtivadoEvent event) {
        System.out.println("Cliente " + event.getCliente().getNome() + " agora está ativo");
        this.notificador.notificar(event.getCliente(), "Seu cadastro no sistema está ativo!");
    }

}
