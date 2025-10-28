# Introdução ao JPA e Hibernate

## 3.2. O que é JPA e Hibernate

### O que é persistência de dados?

É utilizando para dizer que queremos preservar os dados por um tempo além da utilização de um ‘software’. Geralmente
utiliza-se banco de dados para fazer a persistência de dados. 

**Como funciona a persistência com banco de dados usando Java?**

[Código Java] -> [Driver JDBC] -> [MySQl]

**Driver JDBC**: É um componente de ‘software’ que <u>**intermedia o acesso de uma aplicação Java com um banco de dados**.</u> 
Cada banco de dados, **tem ao menos uma implementação de JDBC**. Basicamente, essa implementação vai saber se comunicar com o
MySQL, pois o código Java 'Conversa' apenas com o Driver JDBC que ele faz a comunicação com o banco de dados MySQL.

Exemplo de um possível método para fazer uma adição de pessoa usando um driverJDBC com implementação do MySQL:

```java
public void adicionar(Pessoa pessoa) throws SQLException {
    String sql = "insert into tab_pessoas (nom_pessoa, num_idade) values (?,?)";
    PreparedStatement stm = connection.prepareStatement(sql);
    stm.setString(1, pessoa.getNome());
    stm.setInt(2, pessoa.getIdade());
    
    stm.executeUpdate();
}
```

### O que é ORM?

Significa **Object Relational Mapper** ou Mapeamento Objeto Relacional, é uma técnica para mapeamento de classes (PO) que 
representa entidades para tabela de um banco de dados relacional.

### Mapeamento Objeto-Relacional

| Modelo Relacional | Modelo OO  |
|-------------------|------------|
| Tabela            | Classe     |
| Linha             | Objeto     |
| Coluna            | Atributo   |
|                   | Método     |
| Chave estrangeira | Associação |

O ORM é basicamente essa tabela de d-para. 

| Esse mapeamento é feito por meta-dados que descrevem a relação entre tabela e classe, coluna e atributo, etc.

**Persistência com ORM**

> O código Java não vai mais 'falar' diretamente com o _Driver JDBC_, ele se comunica com a solução de ORM, e o ORM vai 
    transmitir as instruções para o JDBC que vai transmitir para banco de dados. 

[Código Java] -> [ORM] -> [Driver JDBC] -> [MySQl]

Exemplo: 

```java
public void adicionar(Pessoa pessoa) {
    // manager -> Exemplo de uma instância do gerenciador do ORM. 
    manager.persistir(pessoa);
}
```
**Agora, a persistência é feita de Objetos Java**, a solução ORM vai traduzir isso em código SQL executar no banco de dados
e fim. Isso funciona porque já foi feito o mapeamento do objeto relacional. 

### O que é Java Persistence API (JPA)

É uma especificação JEE, uma solução ORM para persistência de dados, padronizada para desenvolvimento de aplicações Java.

Ou seja, o JPA <u>define a forma de mapeamento objeto-relacional</u>, possui API de consultas, modificações de dados
e linguagem de consulta como a JPQL.

> JPA é uma especificação JEE, não é um produto, JPA não funciona sozinha, é uma especificação, 
e uma especificação descreve como algo deve funcionar, não é o produto final.

### O que é Hibernate?

É uma **implementação da especificação JPA**, é o produto.

JPA descreve como vai funcionar a solução e o hibernate implementa a solução conforme as regras definidas pela JPA.

## 3.3. Adicionando JPA e configurando o Data source

Para adicionar no projeto a JPA, precisamos adicionar o seu starter, o **Spring Data JPA**


doc: https://spring.io/projects/spring-data-jpa, já vai adicionar todo o pacote, incluindo a implementação do _hibernate_

1. Adicionando o starter JPA ao projeto no POM.XML

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```
Ao adicionar essa dependência, é necessário também configurar a conexão com o banco de dados. 

### Configurando a conexão com o banco de dados.

1. Adicionar a URL de conexão, pois é por ela que o Driver JDBC vai interpretar o código e conectar no banco de dados.

```application.properties
// Na doc de Mysql, após o ponto de interogação "?" podemos passar propriedades 
spring.datasource.url=jdbc:mysql//localhost:3306/algafood?createDatabaseIfNotExist=true
```

2. Adicionar do Driver do MySQL.

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```
## 3.4. Mapeando entidades com JPA

Para fazermos o mapeamento-objeto-relacional precisamos cria uma classe Java e fazer uma tabela 
com o banco de dados. 

**Existem duas formas de fazer:**

1. Primeiro criar as tabelas e depois criar as entidades, fazendo o mapeamento de acordo com já criado no banco de dados. 
2. Inverter a lógica, criar primeiro as entidades e depois faz o mapeamento com as tabelas no banco de dados.

**Estrutura de pastas**
- domain: Classes do domínio do negócio, tudo que é relacionado a regra de negócio, onde fica a solução do problema de negócio.
- model ou entity: São as entidades, onde vai ficar o módelo de domínio.

Exemplo de mapeamento: 

```java
// javax.persistence -> Vem da especificação da JPA
import javax.persistence;

// Essa classe cozinha vai representar uma tabela no banco de dados chamada cozinha
@Entity // Representa uma entidade e uma tabela (Por padrão o nome da tabela é o nome da classe)
@Table(name = "tab_cozinhas")
public class Cozinha {

    @Id // Informa que esse atributo vai representar o identificador da entidade (chave primária
    private Long id;

    @Column(name = "nom_cozinha")
    private String nome;

    public Long getId() {
        return id;
    }
}
```

## 3.5. Criando as tabelas do banco a partir das entidades

Conseguimos gerar as tabelas do banco de dados de forma automática a partir do mapeamento.
Obs: Usar essa forma só para estudos e em ambientes de desenvolvimento. Não é o recomendado em prod.

Em resumo é só fazer o mapeamento e rodar o projeto. 

## 3.6. Mapeando o id da entidade para autoincremento

É quando queremos deixar a responsabilidade para o próprio banco de dados incrementar o id. (1,2,3..), se não tiver,
toda a vez que se for inserir um novo registro deveremos especificar qual id vai ser utilizado.

### @GeneratedValue(strategy = GenerationType.IDENTITY) // Definindo a geração do valor para ID utilizando a estratégia de IDENTITY

Aqui estamos a informar que estamos a passar a responsabilidade de gerar o valor do identificador para provedor de persistência.

## 3.7. Importando dados de teste com import.sql

Podemos criar um arquivo sql para carregar com dados nas tabelas, para quando iniciar a aplicação tem dados para trabalhar. 

Basta criar o arquivo dentro de src/main/resource, o arquivo deve ser exatamente com o nome (**import.sql**)

## 3.8. Consultando objetos do banco de dados

O **EntityManager** é como se fosse o coração do JPA, é a principal interface usada para **interagir com o banco de dados**. 
Ele gerencia um contexto de persistência, que é basicamente uma "memória" de quais as entidades estão a ser rastreadas no momento.

- Criar 
- Buscar
- Atualizar
- remover 

Essas operações são em cima de objetos que represetam registro na do banco de dados. 

**Como usar?**

Primeiro, é necessário realizar a injeção de uma instância do EntityManager, para isso, utilizamos a anotação
**@PersistenceContext**.

**@PersistenceContext**: Anotação responsável por injetar o EntityManager (Que Já vem com o Spring Data JPA), é forma 
padrão de injeção definida pela própria JPA.

```java
@Component
public class CadastroCozinha {

    @PersistenceContext
    private EntityManager manager;

    public List<Cozinha> listar() {
        // Aqui estamos apenas criando a consulta
        TypedQuery<Cozinha> query = manager.createQuery("from Cozinha", Cozinha.class);

        // com o query criada, é possível executar a consulta
        return query.getResultList();
    }

}
```

Obs: A linguagem utilizada para criação da TypedQuery e pelo EntityManager é a JPQL e não SQL, 
por conta disso não precisamos do `SELECT *`;

Para visualizar o sql que está a ser gerado pelo hibernate, pode se ativar através do `application.properties`
a propriedade `spring.jpa.show-sql=true`

## 3.9. Adicionado um objeto ao banco de dados

Para realizar a inclusão de objeto usando o EntityManager, é necessário acionar o método **merge**.

**merge:** (Significa "fundir", ou seja, colocar a entidade dentro do contexto de persistência), esse método 
vai retornar a instância do objeto persistido.

```java
public Cozinha adicionar(Cozinha cozinha) {
    return manager.merge(cozinha);
}
```

**@Transactional** -> Quando estamos a fazer uma modificação no contexto de persistência, no caso no banco de dados, precisamos de uma transação. 
Utilizamos a anotação @Transactional;

```java
import org.springframework.transaction.annotation.Transactional;

@Transactional // o método anotado vai ser executado numa transação
public Cozinha adicionar(Cozinha cozinha) {
    return manager.merge(cozinha);
}
```

## 3.10. Buscando um objeto pelo id no banco de dados

Basta chamar a instância do EntityManager chamando o método `find`, passando como parâmetro o tipo da entidade 
o primaryKey.

```java
public Cozinha buscar(Long id) {
    return manager.find(Cozinha.class, id);
}
```

## 3.11. Atualizando um objeto no banco de dados

É basicamente chamar o método `merge` do EntityManager passando o id da entidade, dessa forma, ele vai entender 
que eu desejo atualizar o registro. 

```java
    public static void main(String[] args) {
    ConfigurableApplicationContext context = new SpringApplicationBuilder(AlgafoodApiApplication.class)
            .web(WebApplicationType.NONE)
            .run(args);

    CadastroCozinha cadastroCozinha = context.getBean(CadastroCozinha.class);

    Cozinha cozinha = new Cozinha();

    cozinha.setId(1L);
    cozinha.setNome("Brasileira");
    cadastroCozinha.adicionar(cozinha);


    System.out.printf("%d - %s\n", cozinha.getId(), cozinha.getNome());
}
```

## 3.12. Excluindo um objeto do banco de dados

Basta acionar o EntityManager e chamar o método `.remove(Cozinha)` passando a instância da entidade.

Obs: Sempre utilizar a anotação **@Transactional** para ser executado numa transação. 

### Estados de uma entidade (Ciclo de vida)

Uma entidade pode assumir alguns estados com relação ao `EntityManager`: 

- **Novo (_new ou transient_)** -> Quando construimos um objeto usando o `new`
- **Gerenciado (_managed_)** -> São os métodos `persist`, `merge` ou buscar uma entidade usando `EntityManager` 
  - Aqui o objeto vai ser gerenciado pelo contexto de persistência da JPA. Ou seja, qualquer alteração nessa instância 
    vai refletir no banco de dados
  - Uma forma de ter uma instância nesse estádo de `Managed`, é fazer um `find, query, `
- **Removido (_removed_)** -> Quando chamamos o método `remove`, 
  - só é possível passar para o estado 'Removed', se o nosso Objeto estiver no contexto de `Managed` para que o 
    contexto de persistência gerir a instância para depois remover ela. 
- **Desanexado (_detached_)** -> quando é passada para o método detach. 
  - A partir do estado de _managed_ podemos desanexar ele, aqui ele vai se transformar num estado não gerenciado pelo 
    contexto de persistência,
    
Exemplo: 
```java
@Transactional
public void remover(Cozinha cozinha) { // 1 - Estado de transient
    // 2 -> Passando para o estado de managed, objeto gerenciado pela JPA
    cozinha = buscar(cozinha.getId());
    // 3 -> Passando para o estado de removed
    manager.remove(cozinha);
}
```

## 3.13. Conhecendo o padrão Aggregate do DDD

### O que é DDD? 

É uma abordagem de desenvolvimento de Software, que nos ajuda a criar software de alta qualidade, com foco no domínio.

### O que é o Aggregate? 

É um padrão do DDD. É um grupo de objetos de domínio que podem ser tratados como uma única unidade.

Artigo para aprofundamento: https://martinfowler.com/bliki/DDD_Aggregate.html

Um exemplo seria uma classe `Pedido` e a classe `ItemPedido` podem ser consideradas como uma unidade única, no caso seria
PEDIDO, ou seja, **o agregrado vai agrupar duas entidades, a entidade Pedido e a ItemPedido**.

O objetivo principal é deixar claro, que sempre que formos mexer em um `ItemPedido` precisamos modificar o mesmo através
do seu Aggregate Root, no caso, seria atraés do `Pedido`;

**Aggregate Root:** É a raiz do Agregrado. (PAI); qualquer referência de fora do Aggregrado deve ir apenas para o `Aggregate Root`. Esse ainda 
pode garantir a integridade de um agregrado como um todo. 

- Um repository por agregrado. 


## 3.14. Conhecendo e implementando o padrão Repository

Mas um padrão do DDD, ele adiciona uma camada de abstração para acesso a dados. Ele imita uma coleção, de forma que 
quem usa o repositório não precisa saber qual mecânismo de persistência está sendo usado por esse repositório.

**Onde colocar esse repositório no projeto?** 

A ‘interface’ de um repositório é uma abstração do acesso a dados, ou seja, é algo do negócio, da camada de domain. 

Como se trata de uma ‘interface’ que imita uma coleção, não falamos em implementação, e sim em negócio. 

**O que uma cozinha tem que ter?**

- Listar Cozinhas;
- Buscar uma Cozinha;
- Salvar uma Cozinha;
- Remover uma Cozinha;

Exemplo: 

```java
public interface CozinhaRepository {
    List<Cozinha> listar();
    Cozinha buscar(Long id);
    Cozinha salvar(Cozinha cozinha);
    void remover(Cozinha cozinha);
}
```

Como é uma ‘interface’ do domínio, não deve ter detalhes técnicos é agnóstico a mecanismos de persistências. Se vai ser em um banco de dados, arquivo XML e etc.. Isso aqui nesse momento não vai importar. 

### Implementação do repository

package de infrastructure -> repository. 

Não deve ser dentro de domain, pois trata-se de uma classe de implementação técnica de como 
fazer acesso ao banco de dados, não tem nada a ver com o négócio da aplicação. 

```java
@Component
public class CozinhaRepositoruImpl implements CozinhaRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Cozinha> listar() {
        return manager.createQuery("from Cozinha", Cozinha.class)
                .getResultList();
    }

    @Override
    public Cozinha buscar(Long id) {
        return manager.find(Cozinha.class, id);
    }

    @Transactional
    @Override
    public Cozinha salvar(Cozinha cozinha) {
        return manager.merge(cozinha);
    }

    @Transactional
    @Override
    public void remover(Cozinha cozinha) {
        cozinha = buscar(cozinha.getId());
        manager.remove(cozinha);
    }
}
```
Em resumo, o mais importante é **que não se cria um repositório por tabela/entidade e sim por aggregado**, a partir do root.

## 3.15. Conhecendo e usando o Lombok

**Project Lombok** É uma biblioteca Java, com foco em produtividade e redução de código boilerplate, usando anotações próprias.

Para utilizar, basta adicionar a dependência ao POM.XML 

```markdown
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

Após isso é só realizar anotações nas classes, como `@Getter, @Setter, @Data etc.`

## 3.17. Mapeando Relacionamento com @ManyToOne

Relacionamento de muitos para um. Na classe, basta criar uma propriedade do tipo desejado. 
No exemplo de relacionamento entre a entidade `Restaurante` e `Cozinha`. Ou seja, um Cozinha poderá ter vários restaurantes associados a ela.

```java
    @ManyToOne // Muitos Restaurantes possui uma Cozinha
    private Cozinha cozinha;
```

Com essa anotação, o hibernate vai adicionar a coluna e adicionar um FOREIGN KEY (`cozinha_id`).

## 3.18. A anotação @joinColumn

Nos casos das colunas que tem mapeamento como as anotadas com `@ManyToOne` para modificar o nome da coluna é necessário adicionar a anotação `joinColumn(name = "cozinha_id")` -- Por padrão, o nome da coluna é dado pelo nome da propriedade_id

## 3.19. Propriedade nullable de @Column e @joinColumn

De forma padrão, todas as colunas aceitam valores nulos, com exceção da chave primária.
Colunas que são obrigatórios geralmente são `NOT NULL`

Para criar as colunas com `NOT NULL` basta adicionar na anotação `@Column` a propriedade nullable = false;
Exemplo: `@Column(nullable = false)` e `@JoinColumn(nullable = false)`

> Não é só para criar a coluna com NOT NULL, mas também influência o tipo de JOIN.

Caso a criação das tabelas (DDL) tenham sido criadas na mão, não é necessário esse mapeamento. Isso não tem nada a ver
com validações, é só um detalhe físico da tabela, usado para criar tabela. 

## 3.20 - Desafio 

- Criar a entidade ok
- Fazer o mapeamento  ok 
- Criar os repositórios para cada <<Aggregate Root>> ok
- importar dados no import.sql ok

Entidades: 
[x] FormaPagamento 
[x] Permissao 
[x] Estado 
[x] Cidade.