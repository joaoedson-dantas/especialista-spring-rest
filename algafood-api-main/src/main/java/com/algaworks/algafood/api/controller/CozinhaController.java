package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.model.CozinhasXmlWrapper;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cozinhas") // Aqui vai ser informado qual a URI para fazer o mapeamento do controlador.
public class CozinhaController {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;

    // Significa que requisições com o verbo HTTP GET chegaram nesse método
    @GetMapping()
    public List<Cozinha> listar() {
        return cozinhaRepository.findAll();
    }

    // Esse método vai responder apenas quando o consumidor solicitar XML
    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public CozinhasXmlWrapper listarXml() {
        return new CozinhasXmlWrapper(cozinhaRepository.findAll());
    }

    // {cozinhaId} é uma variável, chamada placeholder, onde será feito o binding com o parâmetro do método.
    @GetMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {
        Optional<Cozinha> cozinha = cozinhaRepository.findById(cozinhaId);


//        if (cozinha.isPresent()) {
//            return ResponseEntity.ok(cozinha.get());
//        }
//
//        return ResponseEntity.notFound().build();

        return cozinha.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

        // return ResponseEntity.status(HttpStatus.OK).body(cozinha);
        // return ResponseEntity.ok(cozinha); // atalho para linha de código comentada acima.

        // exemplo de FOUND -> Movido temporariamente para outra URI
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.LOCATION, "http://localhost:8080/cozinhas"); // Informa no cabeçalho location qual a URI do novo lugar
//        return ResponseEntity
//                .status(HttpStatus.FOUND)
//                .headers(headers)
//                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Cozinha adicionar(@RequestBody Cozinha cozinha) {
        return cadastroCozinhaService.salvar(cozinha);
    }

    @PutMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> atualizar(@PathVariable Long cozinhaId, @RequestBody Cozinha cozinha) {
        Optional<Cozinha> cozinhaAtual = cozinhaRepository.findById(cozinhaId);

        if (cozinhaAtual.isPresent()) {
            // cozinhaAtual.setNome(cozinha.getNome());
            // Esse método útil vai copiar os valores da cozinha para cozinhaAtual / o  terceiro parâmetro ele ignora, não vai copiar o id
            BeanUtils.copyProperties(cozinha, cozinhaAtual.get(), "id");

            Cozinha cozinhaSalva = cadastroCozinhaService.salvar(cozinhaAtual.get());
            return ResponseEntity.ok(cozinhaSalva);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> remover(@PathVariable Long cozinhaId) {
        try {
            cadastroCozinhaService.excluir(cozinhaId);
            return ResponseEntity.noContent().build();
        } catch (EntidadeEmUsoException e) {
            // Poderia ser 400 (Bad Request), mas ele é mais abrangente.
            // Ao usar o status 409 (Conflict), é bom retornar um corpo descrevendo qual foi o problema que gerou o conflito. (Modelagem de erro)
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
