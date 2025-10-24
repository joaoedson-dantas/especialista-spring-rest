package com.algaworks.algafood.di.listener;

import com.algaworks.algafood.di.service.ClienteAtivadoEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EmissaoNotaFiscalService {

    @EventListener // Informo para o Spring que esse método é um ouvinte de um evento
    public void clienteAtivadoListener(ClienteAtivadoEvent event) {
        System.out.println("Emitindo Nota fiscal para cliente " + event.getCliente().getNome());
    }
}
