# Super poderes do Spring Data JPA

## 5.1. Implementando consulta JPQL em repositórios

**O que é JPQL?**

É a linguagem do JPA.

`@Repository` -> É uma anotação que serve para dar semântica ao Bean e também tem uma funcionalidade que é um \
tradutor de `exceptions` ou seja, existe uma classe que "intercepta" esses métodos (da classe anotada) e traduz para \
exceptions do próprio Spring. 

`@RequestParam` -> Serve para vincular parâmetros de requisições HTTP a argumentos de métodos.
```java
@GetMapping("/cozinhas/por-nome") // Nome vem por queryString | ?nome=tailandesa
public List<Cozinha> cozinhaPorNome(@RequestParam("nome") String nome) {
    return cozinhaRepository.consultarPorNome(nome);
}
```

**Utilizando a JPQL**

Adiciona o método na interface: 

```java
public interface CozinhaRepository {
    List<Cozinha> consultarPorNome(String nome);
}
```

Realiza a implementação: 

```java
    @Override
public List<Cozinha> consultarPorNome(String nomeCozinha) {
    return manager.createQuery("from Cozinha WHERE nome like :nome", Cozinha.class) // consulta + tipo da classe que será retornada 
            .setParameter("nome", "%" + nomeCozinha + "%") // adicionado o parâmetro 
            .getResultList(); // chama a consulta
}
```

## 5.2. Conhecendo o projeto Spring Data JPA (SDJ)

O Spring Data JPA fornece um repositório de forma genérica, trazendo as ações mais comuns realizadas na camada de dados, \
o objeto é reduzir os códigos "boilerplate". 

É uma classe que implementa funcionalidades comuns, como salvar, excluir, listar, etc.

**Spring Data JPA**

É um projeto dentro do Spring Data. O foco em ajudar a implementar repositórios JPA com muito mais produtividade, \
eliminando boa parte do código "boilerplate".

1. Criar uma ‘interface’ que herda de outra ‘interface’ do próprio spring data JPA 
2. Em tempo de execução, o Spring Data JPA instância uma implementação com vários métodos:

Quando queremos um novo método de consulta que ele não fornece, basta declarar a assinatura desse método na ‘interface’ \
(que não tem implementação) e ele mesmo fornece uma implementação em tempo de execução. 

> Temos apenas uma ‘interface’, para cada repositório, sem classes implementando ela, pois o Spring Data JPA já vai fazer
> essa implementação.

Não perdemos o controle sobre a consulta, ser for necessário podemos métodos com a nossa implementação. 
