package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.model.CozinhasXmlWrapper;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cozinhas") // Aqui vai ser informado qual a URI para fazer o mapeamento do controlador.
public class CozinhaController {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    // Significa que requisições com o verbo HTTP GET chegaram nesse método
    @GetMapping()
    public List<Cozinha> listar() {
        return cozinhaRepository.listar();
    }

    // Esse método vai responder apenas quando o consumidor solicitar XML
    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public CozinhasXmlWrapper listarXml() {
        return new CozinhasXmlWrapper(cozinhaRepository.listar());
    }

    // {cozinhaId} é uma variável, chamada placeholder, onde será feito o binding com o parâmetro do método.
    @GetMapping("/{cozinhaId}")
    public Cozinha buscar(@PathVariable Long cozinhaId) {
        return cozinhaRepository.buscar(cozinhaId);
    }
}
