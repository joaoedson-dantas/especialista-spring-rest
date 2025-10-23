package com.algaworks.algafood.di.notificacao;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/*
*   Classe responsável por representar as configurações do grupo de notificações
* */
@Component
@ConfigurationProperties("notificador.email") // Informamos para o Spring que essa classe representa um arquivo de configuração de propriedades
public class NotificadorProperties {

    /**
     *  Host do servidor de e-mail
     * */
    private String hostServidor;
    /**
     *  Porta do servidor de e-mail
     * */
    private Integer portaServidor = 32; // É possível passar um valor padrão, caso não tenha o application.properties

    public String getHostServidor() {
        return hostServidor;
    }

    public void setHostServidor(String hostServidor) {
        this.hostServidor = hostServidor;
    }

    public Integer getPortaServidor() {
        return portaServidor;
    }

    public void setPortaServidor(Integer portaServidor) {
        this.portaServidor = portaServidor;
    }
}
