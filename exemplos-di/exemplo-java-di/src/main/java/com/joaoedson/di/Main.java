package com.joaoedson.di;

import com.joaoedson.di.modelo.Cliente;
import com.joaoedson.di.notificacao.Notificador;
import com.joaoedson.di.notificacao.NotificadorEmail;
import com.joaoedson.di.notificacao.NotificadorSMS;
import com.joaoedson.di.service.AtivacaoClienteService;

public class Main {
    public static void main(String[] args) {
        Cliente joao = new Cliente("João", "joaoe@email.com", "85974420772");
        Cliente leia = new Cliente("Maria", "maria@email.com", "85974420773");

        Notificador notificador = new NotificadorSMS();
        // Injetando um notificador na instanciação da classe AtivacaoClienteService
        // Isso é a injeção de dependência
        AtivacaoClienteService ativacaoClienteService = new AtivacaoClienteService(notificador);
        ativacaoClienteService.ativar(joao);
        ativacaoClienteService.ativar(leia);
    }
}
