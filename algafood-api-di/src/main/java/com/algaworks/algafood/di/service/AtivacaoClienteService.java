package com.algaworks.algafood.di.service;

import com.algaworks.algafood.di.modelo.Cliente;
import com.algaworks.algafood.di.notificacao.Notificador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class AtivacaoClienteService {

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
                notificador.notificar(cliente, "Seu cadastro no sistema está ativo!");
        } else {
            System.out.println("Não existe notificador, mas cliente foi ativado");
        }
    }
}
