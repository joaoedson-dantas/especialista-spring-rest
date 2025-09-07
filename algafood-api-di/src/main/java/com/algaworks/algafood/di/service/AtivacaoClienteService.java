package com.algaworks.algafood.di.service;

import com.algaworks.algafood.di.modelo.Cliente;
import com.algaworks.algafood.di.notificacao.Notificador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AtivacaoClienteService {

    private Notificador notificador;

    // Injetando um NotificadorEmail via construtor: Que vai receber como parâmetro um objeto a qual o tipo
    // seja gerenciado pelo spring;
    @Autowired // Informa para o Spring que ele deve usar esse ponto de Injeção.
    public AtivacaoClienteService(Notificador notificador) {
        this.notificador = notificador;

        System.out.println("AtivacaoClienteService: " + notificador);
    }


    public AtivacaoClienteService(String qualquer) {

    }

    public void ativar(Cliente cliente) {
        cliente.ativar();

        notificador.notificar(cliente, "Seu cadastro no sistema está ativo!");
    }
}
