package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.algaworks.algafood.infrastructure.repository.spec.RestauranteSpecs.comFreteGratis;
import static com.algaworks.algafood.infrastructure.repository.spec.RestauranteSpecs.comNomeSemelhante;

@RestController
@RequestMapping("/teste")
public class TesteController {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @GetMapping("/cozinhas/por-nome") // Nome vem por queryString | ?nome=tailandesa
    public List<Cozinha> cozinhaPorNome(@RequestParam("nome") String nome) {
        return cozinhaRepository.findTodasCozinhaByNomeContaining(nome);
    }


    @GetMapping("/restaurantes/por-taxa-frete") // Nome vem por queryString | ?nome=tailandesa
    public List<Restaurante> restaurantesPorTaxaFrete(BigDecimal taxaInicial, BigDecimal taxaFinal) {
        return restauranteRepository.findByTaxaFreteBetween(taxaInicial, taxaFinal);
    }

    @GetMapping("/restaurantes/por-nome") // Nome vem por queryString | ?nome=tailandesa
    public List<Restaurante> restaurantesPorTaxaFrete(String nome, Long cozinhaId) {
        return restauranteRepository.findByNomeContainingAndCozinhaId(nome, cozinhaId);
    }

    @GetMapping("/restaurantes/primeiro-por-nome") // Nome vem por queryString | ?nome=tailandesa
    public Optional<Restaurante> restaurantesPorTaxaFrete(String nome) {
        return restauranteRepository.findFirstQualquerCoisaByNomeContaining(nome);
    }

    @GetMapping("/restaurantes/top2-por-nome")
    public List<Restaurante> restaurantesTop2PorNome(String nome) {
        return restauranteRepository.findTop2ByNomeContaining(nome);
    }

    @GetMapping("/restaurantes/por-nome-e-frete")
    public List<Restaurante> restaurantesPorNomeEFrete(String nome,
                                                       BigDecimal taxaFreteInicial,
                                                       BigDecimal taxaFreteFinal) {
        return restauranteRepository.findComCriteria(nome, taxaFreteInicial, taxaFreteFinal);
    }

    // Exemplo de como usaríamos um Specification
    @GetMapping("/restaurantes/com-frete-gratis")
    public List<Restaurante> restaurantesComFreteGratis(String nome) {
        // Aqui teria uma classe que representa uma restrição, um filtro.
        // Dentro dessa classe, teria o código que faríamos a restrição (criando o seu predicado)
        // var comFreteGratis = new RestauranteComFreteGratisSpecification();
        // var comNomeSemelhante = new ComNomeSemelhanteSpec(nome);

        // para fazer a consulta, chamaria o findAll com as resitrições.
        // eturn restauranteRepository.findAll(comFreteGratis.and(comNomeSemelhante));

//        var comFreteGratisSpec = new RestauranteComFreteGratisSpec();
//        var comNomeSemelhanteSpec = new RestauranteComNomeSemelhanteSpec(nome);
//        return restauranteRepository.findAll(comFreteGratisSpec.and(comNomeSemelhanteSpec));

        // Usando a fábrica de Specification
        return restauranteRepository.findComFreteGratis(nome);
    }

    @GetMapping("/restautantes/primeiro")
    public Optional<Restaurante> restaurantesPrimeiro() {
        return restauranteRepository.buscarPrimeiro();
    }
}
