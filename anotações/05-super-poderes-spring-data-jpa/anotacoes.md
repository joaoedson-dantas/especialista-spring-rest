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
o objetivo é reduzir os códigos "boilerplate". 

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

## 5.3. Criando um repositório com Spring Data JPA (SDJ)

Adicionar o projeto do Spring Data JPA: 

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

```

Após isso, deve-se criar uma interface e anotar ela como @Repository e, em seguida, herdar o JpaRepository passando \
o tipo da classe (Entidade JPA) e tipo da chave primária (Id = Long) 

```java
@Repository // Registrar que é um componente do tipo Repository
public interface CozinhaRepository extends JpaRepository<Cozinha, Long> {
    // List<Cozinha> consultarPorNome(String nome);
}
```

Em tempo de execução, o Spring Data JPA instância uma implementação com vários métodos: findAll, save, delete, count, etc.


## 5.4. Refatorando o código do projeto para usar o repositório do SDJ

- **findAll** -> Busca todos os objetos salvos no banco, retorna uma lista.
- **findById** -> Retorno um Optional do objeto buscado. | Usa esse padrão para não retornar null.
- **existsById** -> Verifica se existe | Retorna um boolean
- **save** -> Salva 
- **deleteById** -> Deleta pelo id | quando o id buscado não existe no momento realizar a deleção com o deleteById(), ele simplesmente não deleta e não avisa sobre o fato de não existir.

## 5.6. Criando consultas com query methods

Implementando uma consulta que busca por um nome: 

Quando definimos um método na ‘interface’ do repositório, o Spring Data Jpa vai tentar criar uma implementação 
para esse método, a JPA vai fazer isso conforme as propriedades da entidade. 

```java
@Repository
public interface CozinhaRepository extends JpaRepository<Cozinha, Long> {
    // List<Cozinha> consultarPorNome(String nome);
    List<Cozinha> nome(String nome);
}
```

> O hibernate vai fazer uma pesquisa exata pela propriedade mapeada no método.
> Ex: Hibernate: select cozinha0_.id as id1_1_, cozinha0_.nome as nome2_1_ from cozinha cozinha0_ where cozinha0_.nome=?

Ou seja, não precisamos ter implementação nenhuma, apenas declarar a assinatura do método, já vai fazer uma consulta
usando o critério, no caso do nome. 

**Buscando através do prefixo** + **critérios By** \

> Obs: A partir do 'By' começa os critérios, `findByNome` -> vai buscar pelo nome

- **find:** buscar por 

> Obs: Entre o find e o By podemos adicionar qualquer coisa, com exceção das palavras-chave da JPA - `findQualquerCoisaByNome(String nome)` -
> É uma descrição que serve para nomear o method.

## 5.7. Usando as keywords para definir critérios de query methods

O Spring Data Jpa possui algumas _keywords_, palavras-chave que podemos colocar na assinatura do método para indicar \
alguma coisa. 

- `Containing` -> Adiciona o Like. É tipo que contenha na propriedade.
  - Ex: `List<Cozinha> findTodasCozinhaByNomeContaining(String nome);`
  - Hibernate: select cozinha0_.id as id1_1_, cozinha0_.nome as nome2_1_ from cozinha cozinha0_ where cozinha0_.nome like ? escape ?
 
Link da documentação para saber mais sobre as keywords: \
https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html

- `Between` - Vai buscar entre dois valores
  - List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);
- `And` - Adiciona outra palavra-chave na expressão -> essa outra, é um nome de propriedade ou entidade.
  -  `List<Restaurante> findByContainingAndCozinhaId(String nome, Long cozinhaId);`
  - `Hibernate: select restaurant0_.id as id1_5_, restaurant0_.cozinha_id as cozinha_4_5_, restaurant0_.nome as nome2_5_, restaurant0_.taxa_frete as taxa_fre3_5_ from restaurante restaurant0_ left outer join cozinha cozinha1_ on restaurant0_.cozinha_id=cozinha1_.id where (restaurant0_.nome like ? escape ?) and cozinha1_.id=?`

> O único 'problema' é que os métodos, quando começam a ficar muito grandes, eles acabam por expondo demais as \
> propriedades da entidade e talvez nomes grandes dificulta um pouco a leitura do código.


## 5.8. Conhecendo os prefixos de query methods

Todos têm funcionamento igual. 

- find
- read
- get
- query
- stream

Entre o prefixo e o By podemos colocar um flag. Onde seria um prefixo, uma palavra-chave do Spring Data Jpa.

- First -> Implementa uma consulta filtrando somente o primeiro resultado
  - `Optional<Restaurante> findFirstQualquerCoisaByNomeContaining(String nome);`
  - Hibernate: select restaurant0_.id as id1_5_, restaurant0_.cozinha_id as cozinha_4_5_, restaurant0_.nome as nome2_5_, restaurant0_.taxa_frete as taxa_fre3_5_ from restaurante restaurant0_ where restaurant0_.nome like ? escape ? limit ?

Observe que ele usa o `Limit` para retornar apenas um. 

- Top -> Traz a quantidade que deseja, utilizando o `top2`
  - `List<Restaurante> findTop2ByNomeContaining(String nome);` 
- exists -> Verifica se existe, retorna um boolean
  - `boolean existsByNome(String nome);`
- count -> Conta quantos registros tem e retorna um número com a quantidade de registros. 
  - int countByCozinhaId(Long cozinhaId);

## 5.9. Usando queries JPQL customizadas com @Query

Os _query methods_ são bastantes úteis, mas vão ocorrer momentos onde precisamos de consulta mais complexas \,
além disso, quanto mais complexo vão ficando os 'critérios' o método vai ficar com o nome muito confuso e difícil de manter \
e ainda acaba por expor muito a entidade no método.

Vamos refatorar esse método: 

```java
    List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long cozinhaId);
```

Para funcionar, **é preciso anotar o método com a anotação @Query**

@Query -> Como propriedade dessa anotação, podemos passar uma consulta JPQL 

**Obs:** Para passar os parêmtros recebidos no método, utilizamos os :nomeParametro, conforme exemplo abaixo. 

```java
import org.springframework.data.jpa.repository.Query;

@Query("from Restaurante where nome like %:nome%")
List<Restaurante> consultarPorNome(String nome);
```

Um ponto importante, caso o nome que vem no parâmetro, seja o mesmo utilizado na consulta \
o bind vai funcionar sem configuração adicional, caso contrário, é necessário adicionar uma anotação \
@Param("id"), passando como argumento o nome do parâmetro que queremos fazer o bind.

Ex: 

```java
import org.springframework.data.jpa.repository.Query;

@Query("from Restaurante " +
        " where nome like %:nome% and cozinha.id = :id")
List<Restaurante> consultarPorNome(String nome, @Param("id") Long cozinhaId);
```

