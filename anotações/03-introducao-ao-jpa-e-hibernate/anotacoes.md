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

