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

## 5.11. Implementando um repositório SDJ customizado

As vezes é necessário na consulta adicionar código Java, para consultas muito dinâmicas, executar alguma lógica para \
essa consulta acontecer. Nem sempre será possível apenas utilizando JPA fornece.

**O Spring Data JPA permite criar um repositório customizado, apenas para os métodos necessários**

**Onde criar?**

-- infrastructure -- repository

exemplo: 

```java
public class RestauranteRepositoryImpl { }
```

Essa classe, servirá para implementar um repositório customization de Restaurante. \ 
Deveremos criar um repositório customizado, adicionando o sufixo Impl.

**OBS:** O Sufixo "Impl" é importante para identificar que será uma implementação customizada do Repository

@PersistenceContext -> Serve para fazer a injeção do contexto de persistência, no caso, serve para injetar um EntityManager \
gerenciado pelo container (Spring/Jakarta EE) dentro da sua classe.

**O que é o Persistence Context?**

é como uma “sessão” onde o JPA:

- Gerencia entidades em memória
- Controla o ciclo de vida das entidades (managed, detached, removed)
- Faz dirty checking (detecta mudanças automaticamente)
- Sincroniza o estado das entidades com o banco (flush)
- 
O `EntityManager` é a porta de entrada para esse contexto.

Exemplo: 

```java
@Repository
public class RestauranteRepositoryImpl {

    @PersistenceContext // Serve para fazer a injeção do contexto de persistência
    private EntityManager manager;

    // Nome do método não tem relevância, pode ser qualquer nome
    public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {

        var jpql = "from Restaurante where nome like :nome " +
                "and taxaFrete between :taxaFreteInicial and :taxaFreteFinal";

        return manager.createQuery(jpql, Restaurante.class) // Aqui vai criar a query, passando a consulta em JPQL e o seu tipo de retorno
                .setParameter("nome", "%" + nome + "%") // adicionando os parâmetros da consulta
                .setParameter("taxaFreteInicial", taxaFreteInicial)
                .setParameter("taxaFreteFinal" ,taxaFreteFinal)
                .getResultList(); // Busca o resultado.
    }
}
```

**IMPORTANTE ENTENDER**

Para ser possível utilizar esse método via repositorio original, será necessário criar a assinatura na interface \
Logo, o Spring Data Jpa, vai identificar a assinatura e vai verificar que existe uma implementação customizada, vai \
verficar que dentro dessa implementação customizada, existe um método com a mesma assinatura do método da interface

Dessa forma, vai realizar o vínculo com o método da implementação customizada.

Para isso acontecer é necessário ter o nome do repositório com o sufixo "Impl"

A vantagem que dentro desse método é código Java, podendo ser feito qualquer lógica.

**PROBLEMA**

Caso, mude o nome da assinatura do método, no repositório original ou no Impl, não vai ocorrer erro \
de compilação, logo, para evitar isso, é necessário criar uma interface, essa interface deverá está no pacote de 
domínio.

E, no repositório original, apagamos a assinatura e herdamos do RestauranteRepositoryQueries, que será 
a interface que tem o método find.

Agora, caso mudar o nome, dará um erro de compilação e em tempo de execução.

Exemplo: 

```java
public interface RestauranteRepositoryQueries {
    List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);
}

// uso

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long>, RestauranteRepositoryQueries {}
```
## 5.12. Implementando uma consulta dinâmica com JPQL

Quando queremos implementar consultas dinâmicas com JPQL, precisamos trabalhar com concatenação de strings utilizando
o **StringBuilder** e, para setar os parâmetros, utilizamos uma 
estratégia de HashMap

Exemplo: 

```java
    @Override
public List<Restaurante> findDinamico(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
  StringBuilder jpql = new StringBuilder();
  jpql.append("from Restaurante where 1 = 1 ");

  var parametros = new HashMap<String, Object>();

  if (StringUtils.hasLength(nome)) {
    jpql.append(" and nome like :nome");
    parametros.put("nome", "%" + nome + "%");
  }

  if (taxaFreteInicial != null) {
    jpql.append(" and taxaFrete >= :taxaFreteInicial");
    parametros.put("taxaFreteInicial", taxaFreteInicial);
  }

  if (taxaFreteFinal != null) {
    jpql.append(" and taxaFrete <= :taxaFreteFinal");
    parametros.put("taxaFreteFinal", taxaFreteFinal);
  }

  // Ele cria uma instância de uma consulta tipada
  TypedQuery<Restaurante> query = manager
          .createQuery(jpql.toString(), Restaurante.class);

  parametros.forEach((chave, valor) -> query.setParameter(chave, valor));
  // parametros.forEach(query::setParameter);

  return query.getResultList();
}
```

hasLength -> Verifica se não está nullo e se não está vazio.

## 5.13. Implementando uma consulta simples com Critéria API

**Critéria API** 

É uma API do JPA muito poderosa para criação de queries de forma progamática.

Serve para quando precisamos de consultas complexas e dinâmicas, que com JPQL seria muito 
difícil chegar nesse resultado.

Essa API permite montar uma consulta usando código Java e no final uma query SQL é gerada e
executada no banco de dados. 

Obs: Para consultas muito simples, não vale o esforço o critéria API

**Como usar?** 

O método tem uma sobrecarga que recebe o **CriteriaQuery<T>**`manager.createQuery(CriteriaQuery<T>, Restaurante.class)` 
e vamos usar essa sobrecarga. 

**O que é uma CriteriaQuery?** 

É uma interface, é resposável por montar a estrutra de uma query, a composição das cláusulas. 

O tipo CriteriaQuery tem vários métodos úteis, como `select`, `from` , `where`, isso nos faz entender que, o criteria query
vai ser responsável por compor as cláusulas.

**Como obter uma instância de uma CriteriaQuery?**

Precismos de uma instância de um outro tipo, chamado **CriteriaBuilder**.

Esse builder é uma interface que funciona como uma fábrica, serve para construir elementos que precisamos para fazer consultas \
como os critérios da consulta e a própria _CriteriaQuery_.

A partir do builder, utilizamos o método **builder.createQuery** passando como argumento a classe para o qual queremos
criar uma criteriaQuery.

// Diz assim: Oh Fábrica (CriteriaBuilder), me dar um construtor de cláusulas para model Restaurante.
Ex: `CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class);` 

**De onde buscar um builder?**

Obtemos um builder a partir do EntityManager;
Ex: `CriteriaBuilder builder = manager.getCriteriaBuilder();` 

Exemplo de uso: 

```java
public List<Restaurante> findComCriteria(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
    /*
     *  É a "fábrica". Você o usa para criar a query em si e para construir
     *  as cláusulas (filtros como equal, like, greaterThan).
     * */
    CriteriaBuilder builder = manager.getCriteriaBuilder();

    /*
     *  Representa a estrutura da sua consulta (o que você quer selecionar, como quer ordenar, etc.).
     * */
    CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class);
    criteria.from(Restaurante.class);
    
    // Cria uma instância de uma consulta tipada
    TypedQuery<Restaurante> query = manager.createQuery(criteria);

    // Retorna uma lista de restaurante, é o tipo do TypedQuery
    return query.getResultList();
}
```
## 5.14. Adicionando restrições na clásula where com Critéria API

Adicionamos a clásula where utilizando um método do **CriteriaQuery**, o método where(); \
`criteria.where(Predicate... restrictions);` esse método, recebe um varArgs (um array de predicados).

### O que é um predicado?

Predicado é um critério, como um filtro. 

**Como conseguimos uma instância desse Predicate?**

Utilizamos o builder, do CriteriaBuilder, ele é uma fábrica que vai nos dar o que for necessário 
para construção de uma consulta utilizando o criteria. 

Nos métodos de cláusulas, passamos como argumento no primeiro parâmetro a propriedade e o segundo parâmetro o valor. 

**Propriedade** -> De qual entidade vem essa propriedade? Lembre-se, é a propriedade em sí da entidade.

Para pegar essa propriedade, buscamos ela a partir do root do tipo Restaurante. 

### **O que é Root?**
/*
*  Root - É a origem dos dados. Pense nele como o FROM do SQL. Ele define sobre qual entidade você está pesquisando.
* */
`Root<Restaurante> root = criteria.from(Restaurante.class);`

A partir desse root, podemos pegar o atributo, que vai ser uma String. 
`Predicate nomePredicate = builder.like(root.get("nome"), )` -> Aqui é como se fosse: Restaurante.nome 

*root.get("nome")** -> Isso é a representação da propriedade nome, dentro do root<Restaurante>

### Segundo parâmetro, **valor**:

Colocamos exatamente o valor que queremos filtrar, geralmente o que recebemos como parametro da consulta.

`Predicate nomePredicate = builder.like(root.get("nome"), nome);`

Exemplo: 

```java
public List<Restaurante> findComCriteria(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
    /*
     *  CriteriaBuilder - É a "fábrica". Você usa-o para criar a query em si e para construir
     *  as cláusulas (filtros como equal, like, greaterThan).
     * */
    CriteriaBuilder builder = manager.getCriteriaBuilder();

    /*
     *  CriteriaQuery - Representa a estrutura da sua consulta (o que você quer selecionar, como quer ordenar, etc.).
     * */
    CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class);

    /*
    *  Root - É a origem dos dados. Pense nele como o FROM do SQL. Ele define sobre qual entidade você está pesquisando.
    * */
    Root<Restaurante> root = criteria.from(Restaurante.class);

    // Predicado é um critério, como um filtro.
    Predicate nomePredicate = builder.like(root.get("nome"), "%" + nome + "%");

    // greaterThanOrEqualTo = Maior ou igual a = >=
    Predicate taxaInicialPredicate = builder
            .greaterThanOrEqualTo(root.get("taxaFrete"), taxaFreteInicial);

    // lessThanOrEqualTo -> Menor ou igual
    Predicate taxaFreteFinalPredicate = builder
            .lessThanOrEqualTo(root.get("taxaFrete"), taxaFreteFinal);

    // Passando os predicados, as restrições para o wehre.
    // Quando passamos 3 predicados, ele vai fazer um "AND"
    criteria.where(nomePredicate, taxaInicialPredicate, taxaFreteFinalPredicate);

    // Cria uma instância de uma consulta tipada
    TypedQuery<Restaurante> query = manager.createQuery(criteria);

    // Retorna uma lista de restaurante, é o tipo do TypedQuery
    return query.getResultList();
}

```

**lessThanOrEqualTo -> Menor ou igual = <=**
**greaterThanOrEqualTo = Maior ou igual a = >=**

## 5.15. Tornando a consulta com Criteria API com filtros dinâmicos

Colocando os "ifs" para colocar a consulta de forma dinâmica. 

-> Precisamos continuar instânciado os predicados, mas adicionar eles numa lista, para, no final adicionar a lista \
na clásula where.

```java
var predicates = new ArrayList<Predicate>();

if (StringUtils.hasLength(nome)) {
  // Predicado é um critério, como um filtro.
  predicates.add(builder.like(root.get("nome"), "%" + nome + "%"));
}

if (taxaFreteInicial != null) {
  // greaterThanOrEqualTo = Maior ou igual a = >=
  predicates.add(
  builder.greaterThanOrEqualTo(root.get("taxaFrete"), taxaFreteInicial)
  );
}

if (taxaFreteFinal != null) {
  // lessThanOrEqualTo -> Menor ou igual
  predicates.add(
        builder.lessThanOrEqualTo(root.get("taxaFrete"), taxaFreteFinal)
  );
}

criteria.where(predicates.toArray(new Predicate[0]));
```

## 5.16. Conhecendo o uso do padrão Specifications (DDD) com SDJ

É um padrão de projeto, que faz parte do DDD, e o Spring Data JPA implementou esse conceito a
sua API, para ter suporte a mais uma forma de criar consultas dinâmicas.

**Um Specification encapsula uma restrição**, ou seja, um filtro, que pode ser combinado com 
outras specifications e assim se forma uma combinação de restrições.

O objetivo é deixar o código mais fluído e elegante, criando as restrições.

**O Specification Pattern** serve para _**encapsular regras de negócio**_ (ou critérios) \
 em objetos reutilizáveis, combináveis e testáveis.

Em vez de espalhar regras em ifs, queries ou serviços, você cria especificações que respondem à pergunta:

“Esse objeto satisfaz essa regra?”

**Definição formal (Eric Evans)**

| A Specification **afirma um _predicado_ sobre um objeto**. Ela pode ser combinada com outras Specifications e **pode ser usada para validação, seleção ou criação de objetos**.


```java
// Aqui teria uma classe que representa uma restrição, um filtro.
// Dentro dessa classe, teria o código que faríamos a restrição (criando o seu predicado)
var comFreteGratis = new RestauranteComFreteGratisSpecification();
var comNomeSemelhante = new ComNomeSemelhanteSpec(nome);

// para fazer a consulta, chamaria o findAll com as resitrições.
return restauranteRepository.findAll(comFreteGratis.and(comNomeSemelhante));
````

## 5.17. Implementando Specifications com SDJ

A criação dos specifications devem está no pacote _infra_ -> _repository_ -> spec.

Consideremos como infra, pois tem código se comunicando diretamente com a JPA. 

Para utilizar, precisamos criar uma classe implementando a ‘interface’ Specification<T> e
implementar o método `toPredicate`, ou seja, é um método que vai criar um `predicate`.

### Criando um Specificação

```java
/*
 *  Essa é a especificação do domínio Restaurante, é uma regra de negócio e
 *  toda a vez que precisar mudar, mudaríamos somente a especificação.
 *
 *  Ela vai representar um filtro, onde taxaFrete é zero.
 * */
public class RestauranteComFreteGratisSpec implements Specification<Restaurante> {

  /*
   *  Método responsável por criar um predicate.
   *  Predicate: Predicado é um critério, como um filtro.
   *
   *  Esse método, já recebe o root, criteriaQuery e Builder
   *
   *  root: É a origem dos dados. Pense nele como o FROM do SQL. Ele define sobre qual entidade você está pesquisando.
   *   A partir desse root, podemos pegar o atributo, que vai ser uma String.
   *
   *  CriteriaQuery - Representa a estrutura da sua consulta (o que você quer selecionar, como quer ordenar, etc.).
   *
   *  CriteriaBuilder -> É a "fábrica". Você usa-o para criar a query em si e para construir as cláusulas (filtros como equal, like, greaterThan).
   * */
  @Override
  public Predicate toPredicate(Root<Restaurante> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    // Aqui, vou ter que criar a condição da especificação, que representa um filtro onde taxaFrete é zero.
    return criteriaBuilder.equal(root.get("taxaFrete"), BigDecimal.ZERO);
  }
}
```

### Utilizando as Specs

```java
@GetMapping("/restaurantes/com-frete-gratis")
public List<Restaurante> restaurantesComFreteGratis(String nome) {
    
    // Specifications
    var comFreteGratisSpec = new RestauranteComFreteGratisSpec();
    var comNomeSemelhanteSpec = new RestauranteComNomeSemelhanteSpec(nome);

    
    // Fazendo a consulta, chamando o findAll com as restrições 
    return restauranteRepository.findAll(comFreteGratisSpec.and(comNomeSemelhanteSpec));
}
````

**OBS** Para funcionar, é necessário o repositório está preparado para receber um Specification

### Preparando o repostório para receber um spec 

O Repositório terá que herdar a interface `JpaSpecificationExecutor<T>`.

Essa interface possui métodos que recebe como parâmetro um Specification. 

Obs: Cada Specification, cria um predicate do JPA.

## 5.18. Criando uma fábrica de Specifications

Ao invés de instânciar cada classe de Specification, será criado outra classe, onde cada
método dessa classe será uma fábrica de um Specification específico.

Para chamar o método no seu repostório, pode se utilizar da importação estática. 

```java

/*
 * Importação de forma estática (static import)
 *  é um recurso que permite acessar membros static (atributos ou métodos) de uma 
 *  classe sem precisar referenciar o nome da classe toda a vez.
 * 
 *  Ela foi introduzida no Java 5 e o objetivo principal é reduzir verbosidade e melhorar a legibilidade 
 *  quando você usa muitos membros estáticos da mesma classe.
 * */ 
import static com.algaworks.algafood.infrastructure.repository.spec.RestauranteSpecs.comFreteGratis;

@RestController
@RequestMapping("/teste")
public class TesteController {
    @GetMapping("/restaurantes/com-frete-gratis")
    public List<Restaurante> restaurantesComFreteGratis(String nome) {
        return restauranteRepository.findAll(comFreteGratis());
    }
}
```

### Fabrica de Specifications

```java

public class RestauranteSpecs {

    public static Specification<Restaurante> comFreteGratis() {
        /*
         * A interface Specification define apenas um método abstrato: toPredicate.
         *
         * Esse método recebe três parâmetros:
         * - Root<T>
         * - CriteriaQuery<?>
         * - CriteriaBuilder
         *
         * Como Specification é uma interface funcional,
         * ela pode ser implementada utilizando expressão lambda.
         *
         * Portanto, a lambda abaixo representa a implementação
         * do método toPredicate.
         */
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("taxaFrete"), BigDecimal.ZERO);

    }

    public static Specification<Restaurante> comNomeSemelhante(String nome) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("nome"), "%" + nome + "%");
    }
}
```

## 5.19. Injetando o próprio repositório na implementação customizada e a anotação @Lazy

Quando utilizamos as Specifications, trazemos a responsabilidade de adicionar os filtros por parte \
de quem está usando o repositório e não pelo repositório em sí.

- Pode gerar duplicidade de código

Podemos continuar a usar o padrão Specification, mas deve-se criar um método no
repositorio como um ponto central para fazer a busca e esse método do repositório que vai usar o Specification

O local adequado para criação desse método é criando a definição na interface `RestauranteRepositoryQueries` \ 
e implementando no Impl.

Ex: 

```java
public interface RestauranteRepositoryQueries {
    List<Restaurante> findComFreteGratis(String nome);
}
```

**Implementação**

```java
@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {
    @Override
    public List<Restaurante> findComFreteGratis(String nome) {
        return manager.findAll(
                comFreteGratis().and(comNomeSemelhante(nome)));
    }
}
```

Contudo, como estamos dentro `RestauranteRepositoryImpl` não temos acesso ao método `findAll` que recebe \
como argumentos as Specifications. Nesse caso, na classe de implementação (`RestauranteRepositoryImpl`),
precisamos injetar o `RestauranteRepository`, mas, caso tentarmos inicializar a aplicação vai ocorrer a seguinte exceção:

`Relying upon circular references is discouraged and they are prohibited by default.`

Uma referência circular ocorre quando o Bean A depende do Bean B,
e o Bean B depende do Bean A, formando um ciclo de dependências.

Nesse cenário, o Spring não consegue concluir o ciclo de criação
dos beans, pois cada um precisa do outro para ser instanciado
ou inicializado, o que torna a resolução das dependências impossível
(pincipalmente no caso de injeção via construtor).

Para solucionar esse problema podemos usar a anotação `@Lazy` na propriedade injetada.
Ou seja, estamos falando para o Spring só instânciar a dependência no momento que for preciso. 
Ocorre uma iniciação de forma preguiçosa.

```Java
import org.springframework.context.annotation.Lazy;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

  @Autowired
  @Lazy
  private RestauranteRepository restauranteRepository;

  @Override
  @Lazy
  public List<Restaurante> findComFreteGratis(String nome) {
    return restauranteRepository.findAll(
            comFreteGratis().and(comNomeSemelhante(nome)));
  }
}
```

Agora, a reponsabilidade está no repository e o controller só precisa chamar essa implementacão.

`return restauranteRepository.findComFreteGratis(nome);`

## 5.20. Estendendo o JpaRepository para customizar o repositório base

Da forma que está hoje, sempre que precisarmos de uma nova consulta, será necessário customizar 
a interface do repositorio e talvez também implementar o método.

Para fazer isso, é necessário criar um interface, essa interface vai herdar JpaRepository (A mesma herdada no repostiorio original)

Exemplo: 


```java
// Informamos para o Spring que não deve ser levando em conta para fim de instanciação
// de um repositório pelo Spring Data Jpa, ou seja,
// o Spring Data Jpa não deve instânciar uma implementação para essa interface, deve ignorar.
@NoRepositoryBean
public interface CustomJpaRepository<T, ID> extends JpaRepository<T, ID> {

    Optional<T> buscarPrimeiro(String nome);

}

/*
*  CustomJpaRepository<T, ID> -> Quando eu herdar essa interface é necessário especificar os parâmetros de tipo.
*  Para informar o tipo da Entidade e o tipo do ID da entidade
*  extends JpaRepository<T, ID> -> Após isso, repasamos o que estamos recebendo via generic para o interface repository
* */
```

Agora, no `RestauranteRepository` ao invés de herdar `JpaRepository`, vou herdar o `CustomJpaRepository` que por sua vez 
já herda o `JpaRepository` mantendo assim os mesmos métodos. 

Para funcionar precisamos criar a implementação do método declarado no `CustomJpaRepository`

```java
public class CustomJpaRepositoryImpl<T, D> extends SimpleJpaRepository<T, D> implements CustomJpaRepository<T, D> {

    private EntityManager entityManager;

    public CustomJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @Override
    public Optional<T> buscarPrimeiro(String nome) {
        var jpql = "from " + getDomainClass().getName();
        T entity = entityManager
                .createQuery(jpql, getDomainClass()) // Para pegar a entidade TIPADA (T)
                .setMaxResults(1) // vai gerar uma consulta SQL usando o LIMIT de apenas uma linha
                .getSingleResult();

        // ofNullable - Retornando um optional (pode ter um valor nulo dentro desse retorno)
        return Optional.ofNullable(entity);
    }
}

// getDomainClass().getName() => Pegando o nome da classe para qual esse repositório exige = T
```

Após isso será necessário ativar o repositório customizado, para mostrar ao Spring Data Jpa que temos 
um outro repositorio base customizado.

Adicionamos a anotação na classe SpringBootApplication:

`@EnableJpaRepositories@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class)`

Dessa forma substituimos a implementação do repositório base.


