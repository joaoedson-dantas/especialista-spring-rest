package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @GetMapping
    public List<Restaurante> listar() {
        return restauranteRepository.findAll();
    }

    @GetMapping("/{restauranteId}")
    public ResponseEntity<Restaurante> buscar(@PathVariable Long restauranteId) {
        return restauranteRepository.findById(restauranteId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante) {
        try {
            restaurante = cadastroRestauranteService.salvar(restaurante);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(restaurante);
        } catch (EntidadeNaoEncontradaException e) {
            // Aqui não deve ser um 404 - Dar a entender que o recurso - POST /restaurantes não existe.
            // Se a Cozinha não existe, não é um problema de 404 - NOT FOUND -
            // É um problema de requisição inválida. - O ideal seria o 400 - BadRequest - (Algo informado está com problema)
            // O ideal ao ter um badRequest é que se coloque no corpo uma mensagem que ajude o consumidor da API a resolver o problema.
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage()); // De forma temporária.
        }
    }

    @PutMapping("{restauranteId}")
    public ResponseEntity<?> atualizar(@PathVariable Long restauranteId, @RequestBody Restaurante restaurante) {
        try {
            Optional<Restaurante> restauranteAtual = restauranteRepository.findById(restauranteId);

            if (restauranteAtual.isPresent()) {
                BeanUtils.copyProperties(restaurante, restauranteAtual.get(), "id", "formasPagamento");

                Restaurante restauranteSalvo = cadastroRestauranteService.salvar(restauranteAtual.get());
                return ResponseEntity.ok(restauranteSalvo);
            }

            return ResponseEntity
                    .notFound()
                    .build();
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PatchMapping("/{restauranteId}")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long restauranteId,
                                              @RequestBody Map<String, Object> campos) {
        Optional<Restaurante> restauranteAtual = restauranteRepository.findById(restauranteId);

        if (restauranteAtual.isEmpty()) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        // Atribuir cada valor ao restaurante atual, é preciso fazer um merge. Pegar o que tem dentro do Mapa e atribuir
        // dentro do restaurante atual
        merge(campos, restauranteAtual.get());

        return atualizar(restauranteId, restauranteAtual.get());
    }

    @DeleteMapping("/{restauranteId}")
    public ResponseEntity<Cozinha> remover(@PathVariable Long restauranteId) {
        try {
            cadastroRestauranteService.excluir(restauranteId);
            return ResponseEntity.noContent().build();
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino) {
        // Criando uma instância de ObjectMapper | Responsável por converter (serializer - converter objetos Java em JSON e vise versa)
        ObjectMapper objectMapper = new ObjectMapper();
        // ObjectMapper cria pra mim uma instância do tipo restaurante usando como base, esse mapa com os dadosOrigem.
        Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);


        dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
            // Fild (Java Lang - API de reflections do Java)
            // Busca a instância do campo - Essa variável vai representar um atributo da classe Restaurante (Classe que queremos modificar)
            Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade); // Busca pra mim um campo uma variável dessa classe Restaurante chamada (NomeDaPropriedade) | Aqui teremos um campo representando uma propriedade da classe.
            field.setAccessible(true); // Tornará a variável acessível, mesmo que ela seja privada. QUEBRA O ACESSO

            // Esse novo valor já vai está convertido, pois foi utilizado um ObjectMapper
            // Caso não se converter antes, daria algumas exceções
            Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

            // Utiliza a instância do campo - Atribuindo o campo, no objeto destino o valor da propriedade.
            ReflectionUtils.setField(field, restauranteDestino, novoValor);
        });
    }
}
