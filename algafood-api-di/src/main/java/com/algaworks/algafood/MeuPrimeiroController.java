package com.algaworks.algafood;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // -> Essa anotação transforma essa classe num componente gerenciado pelo Spring
// e informa para o framework que essa classe é responsável por receber e responder requisições web (http)
public class MeuPrimeiroController {

    @GetMapping("/hello") // Caminho para chegar no método
    @ResponseBody // Informa que o retorno do método seja devolvido como resposta da requisição
    public String hello() {
        return "Hello Word!";
    }
}
