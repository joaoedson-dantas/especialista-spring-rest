package com.algaworks.algafood.di.notificacao;

import com.algaworks.algafood.di.modelo.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

// @Component // Informa que essa classe vai ser um componente Spring (bean) onde vai passar a ser gerenciado pelo ‘Framework’
// @Qualifier("normal")
@TipoDoNotificador(NivelUrgencia.NORMAL)
public class NotificadorEmail implements Notificador {

//    @Value("${notificador.email.host-servidor}")
//    private String host;
//    @Value("${notificador.email.porta-servidor}")
//    private Integer porta;
    @Autowired
    private NotificadorProperties properties;

    // Propriedades que servem como configuração do NotificadorEmail
    private boolean caixaAlta;
    private String hostServidorSmtp;

    public NotificadorEmail(String hostServidorSmtp) {
        this.hostServidorSmtp = hostServidorSmtp;
        System.out.println("NotificadorEmail ");
    }

    @Override
    public void notificar(Cliente cliente, String mensagem) {
        System.out.println("HOST: " + properties.getHostServidor());
        System.out.println("PORTA: " + properties.getPortaServidor());

        if (caixaAlta) {
            mensagem.toUpperCase();
        }

        System.out.println("Notificador de Email REAL - prod");
        System.out.printf("Notificando %s através do e-mail %s: usando SMTP %s %s\n",
                cliente.getNome(), cliente.getEmail(), this.hostServidorSmtp, mensagem);
    }

    public void setCaixaAlta(boolean caixaAlta) {
        this.caixaAlta = caixaAlta;
    }

}
