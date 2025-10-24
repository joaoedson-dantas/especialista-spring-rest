package com.joaoedson.di.service;

import com.joaoedson.di.modelo.Cliente;
import com.joaoedson.di.notificacao.Notificador;
import com.joaoedson.di.notificacao.NotificadorSMS;

// Classe de serviço responsável por ativar um cliente.
public class AtivacaoClienteService {

    // dependência da classe e a classe não fica mais responsável por instânciar e decidir quem vai ser o notificador.
    // "Me passe esse notificador que eu simplemente vou seguir o seu contrato.
    private final Notificador notificador;

    // Inversão de controle e injeção de dependência: "Esse classe só irá funcionar se quem for usar ela passar a dependência"
    // Nesse caso, o Notificador tornou-se uma dependência
    public AtivacaoClienteService(Notificador notificador) {
        this.notificador = notificador;
    }

    public void ativar(Cliente cliente) {
        if (cliente != null && !cliente.isAtivo()) {
            cliente.ativar();

            // new => Forte acoplamento
            // Nesse exemplo, a classe AtivacaoClienteService depende diretamente de outra classe (NotificadorSMS), causando assim um
            // forte acoplamento. Para solucionar, a classe AtivacaoClienteService deve
            // depender de uma abstração que defina um contrato
            //NotificadorSMS notificador = new NotificadorSMS();
            this.notificador.notificar(cliente, "Seu cadastro no sistema está ativo.");
        }
    }
}
