package com.algaworks.algafood.di.service;

import com.algaworks.algafood.di.modelo.Cliente;

public class ClienteAtivadoEvent {
    private Cliente cliente;

    public ClienteAtivadoEvent(Cliente cliente) {
        // Propriedade que representa o Cliente que acabou de ficar ativo
        this.cliente = cliente;
    }

    public Cliente getCliente() {
        return cliente;
    }
}
