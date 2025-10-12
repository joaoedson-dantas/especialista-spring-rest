package com.algaworks.algafood.di.notificacao;

import com.algaworks.algafood.di.modelo.Cliente;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

// @Component // Informa que essa classe vai ser um componente Spring (bean) onde vai passar a ser gerenciado pelo Framework
@Qualifier("normal")
public class NotificadorEmail implements Notificador {

    // Propriedades que servem como configuração do NotificadorEmail
    private boolean caixaAlta;
    private String hostServidorSmtp;

    public NotificadorEmail(String hostServidorSmtp) {
        this.hostServidorSmtp = hostServidorSmtp;
        System.out.println("NotificadorEmail ");
    }

    @Override
    public void notificar(Cliente cliente, String mensagem) {
        if (caixaAlta) {
            mensagem.toUpperCase();
        }

        System.out.printf("Notificando %s através do e-mail %s: usando SMTP %s %s\n",
                cliente.getNome(), cliente.getEmail(), this.hostServidorSmtp, mensagem);
    }

    public void setCaixaAlta(boolean caixaAlta) {
        this.caixaAlta = caixaAlta;
    }

}
