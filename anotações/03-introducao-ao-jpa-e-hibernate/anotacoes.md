# Introdução ao JPA e Hibernate

## 3.2. O que é JPA e Hibernate

### O que é persistência de dados?

É utilizando para dizer que queremos preservar os dados por um tempo além da utilização de um ‘software’. Geralmente
utiliza-se banco de dados para fazer a persistência de dados. 

**Como funciona a persistência com banco de dados usando Java?**

[Código Java] -> [Driver JDBC] -> [MySQl]

**Driver JDBC**: É um componente de ‘software’ que <u>**intermedia o acesso de uma aplicação Java com um banco de dados**.</u> 
Cada banco de dados, tem ao menos uma implementação de JDBC. Basicamente, essa implementação vai saber se comunicar com o
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

O código Java não vai mais 'falar' diretamente com o _Driver JDBC_, ele se comunica com a solução de ORM, e o ORM vai 
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

