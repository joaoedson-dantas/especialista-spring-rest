package com.algaworks.algafood.di.notificacao;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Qualifier //
@Retention(RetentionPolicy.RUNTIME) // Informa quanto tempo essa anotação (TipoDoNotificador) deve permanecer onde ela foi usada
// Runtime -> Informa que ela pode ser lida em tempo de execução
public @interface TipoDoNotificador {

    // Essa anotação vai receber como parâmetro o nível de urgência
    NivelUrgencia value(); // value -> Valor padrão quando não é passado o valor para anotação

}
