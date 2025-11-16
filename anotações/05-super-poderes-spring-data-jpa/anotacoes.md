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