package com.joaoedson.di.notificacao;

import com.joaoedson.di.modelo.Cliente;

public interface Notificador {
    void notificar(Cliente cliente, String mensagem);
}
