package com.algaworks.algafood.di.notificacao;

import com.algaworks.algafood.di.modelo.Cliente;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component // Informa que essa classe vai ser um componente Spring (bean) onde vai passar a ser gerenciado pelo Framework
// @Primary // Informa que esse bean vai ter prioridade e vai ser utilizado para fazer desambiguação
@TipoDoNotificador(NivelUrgencia.URGENTE)
@Profile("dev")
public class NotificadorSMSMock implements Notificador {

    public NotificadorSMSMock() {
        System.out.println("NOTIFICADO SMS = MOCK");
    }

    @Override
    public void notificar(Cliente cliente, String mensagem) {
        System.out.printf("MOCK: Notificadoria o %s via SMS através do telefone %s: %s\n",
                cliente.getNome(), cliente.getTelefone(), mensagem);
    }
}
