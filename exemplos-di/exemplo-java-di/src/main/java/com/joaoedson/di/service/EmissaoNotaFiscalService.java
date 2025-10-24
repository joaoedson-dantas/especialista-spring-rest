package com.joaoedson.di.service;

import com.joaoedson.di.modelo.Cliente;
import com.joaoedson.di.modelo.Produto;
import com.joaoedson.di.notificacao.Notificador;
import com.joaoedson.di.notificacao.NotificadorEmail;

public class EmissaoNotaFiscalService {

    private final Notificador notificador;

    // Recebendo o notificar via parâmetro no construtor uma abstração que poderá ser qualquer notificador,
    // ou seja, quem for utilizar essa classe vai ser obrigado a prover (injetar) qual tipo de notificador vai querer usar.
    // Aqui ocorre a inversão de controle, onde a dependência foi invertida e
    // ao passo que ouve a sua aplicação concreta com a injeção de dependência
    public EmissaoNotaFiscalService(Notificador notificador) {
        this.notificador = notificador;
    }

    public void emitirNotaFiscal(Cliente cliente, Produto produto) {
        // TODO emite nota fiscal aqui...

        // new => Forte acoplamento
        // Nesse exemplo, a classe EmissaoNotaFiscalService depende diretamente de outra classe, causando assim, um
        // forte acoplamento. Para solucionar, a classe EmissaoNotaFiscalService deve
        // depender de uma abstração que defina um contrato
        //NotificadorEmail notificador = new NotificadorEmail();

        // dependendo de uma abstração, dessa forma, a classe não tem mais a dependência de NotificadorSMS ou NotificadorEmail
        this.notificador.notificar(cliente, "Nota fiscal do produto" +
                produto.getNome() + " foi emitida");
    }
}
