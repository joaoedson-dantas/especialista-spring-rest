package com.algaworks.algafood.di;

import com.algaworks.algafood.di.modelo.Cliente;
import com.algaworks.algafood.di.service.AtivacaoClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // -> Essa anotação transforma essa classe num componente gerenciado pelo Spring
// e informa para o framework que essa classe é responsável por receber e responder requisições web (http)
public class MeuPrimeiroController {

    private AtivacaoClienteService ativacaoClienteService;

    public MeuPrimeiroController(AtivacaoClienteService ativacaoClienteService) {
        this.ativacaoClienteService = ativacaoClienteService;

        System.out.println("MeuPrimeiroController " + ativacaoClienteService);
    }

    @GetMapping("/hello") // Caminho para chegar no método
    @ResponseBody // Informa que o retorno do método seja devolvido como resposta da requisição
    public String hello() {
        Cliente joao = new Cliente("João", "joaoedson.dantas@outlook.com", "85892791283");
        ativacaoClienteService.ativar(joao);
        return "Hello Word!";
    }
}
