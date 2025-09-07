# Spring e Injeção de Dependências

## O Ecossistema Spring

O Ecossistema Spring é uma **coleção de projetos** e ferramentas que facilita a criação de aplicações Java. Hoje, o Spring possui suporte para linguagens como o Kotlin e o Groovy que também rodam na JVM. 

Alguns projetos Spring: 

doc: https://spring.io/projects/
- `Spring Framework`: É a base do ecossistema Spring e serve como base para os demais projetos, ele oferece um conjunto de bibliotecas e APIs que simplificam o desenvolvimento de aplicações Java, com foco em inversão de controle (IoC) e Injeção de Dependência (DI) para criar código mais limpo e modular. **O Spring Framework** possui o **Spring Core** que fornece os blocos de construção essenciais e os princípios fundamentais (como IoC e DI). Dentro do Spring Framework possui outros projetos como:
    - Spring MVC e WebFlux
    - Testing
    - Spring Core (Já comentado acima)
- `Spring Data`: Simplifica o acesso a dados trazendo uma abstração de diferentes tecnologias de persistência, como o JPA (Java Persistence API), mongodb, redis e outros.
- `Spring Security`: Spring Security é um framework focado em fornecer autenticação e autorização para aplicações Java. 
- `Spring Boot`: É uma extensão do **Spring Framework**. É uma camada de abstração em cima do Spring. É um projeto que possui uma visão opinativa, resolvendo os problemas e simplificando a configuração de qualquer outro projeto do spring. O Spring Boot apenas configura o projeto automaticamente usando a sua visão opinativa (Convenções mais usadas no mercado) sobre a criação de aplicações prontas para produção. 
  - `Starter`: O Spring Boot possui os _**starters**_ que são dependências que agrupam outras dependências, se precisar trabalhar com a JPA, por exemplo, é só adicionar o starter no `maven` e todas as dependências necessárias para utilizar a JPA serão adicionadas ao projeto. 
```Markdown
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>sprig-boot-starter-jpa</artifactId>
    </dependency>
```

## 2.6. Conhecendo o Maven e o pom.xml de um projeto Spring Boot

Maven é uma ferramenta de gerenciamento de projetos e compilação que utiliza um arquivo pom.xml para definir dependências e a estrutura do projeto.

Estrutura de pastas padronizada pelo maven: 


- src/main/java -> Código Fonte da aplicação 
- src/main/resources -> Arquivos de propriedades, configurações, html, images e etc.
- pom.xml -> Project Object Model, é onde ficam as configurações do Maven no projeto. 
- HELP.md -> Arquivo a qual podemos colocar uma introdução sobre o projeto 
- mvmw.cmd -> é a sigla para **Maven Wrapper**, que é um script que permite executar um projeto Maven sem a necessidade de ter o maven instalado globalmente no seu computador. cmd -> Windows
- mvnw -> em sistemas Unix/Linux/macOS
  - Para usar, basta abrir o terminal e executar o comando no Wrapper
  - ```BashF\  
        ./mvnw package
    ```
    
Após gerar a build utilizando o comando `./mvnw package`, vai ser gerado o pacote compilado na pasta `target`, passando assim a ser possível a execução do projeto utilizando a JRE.

```Bash
    java -jar target/algafood-api-0.0.1-SNAPSHOT.jar
```

## 2.7. Criando uma Controller com Spring MVC

`@Controller ` -> Ao usar essa anotação, é informado para o Spring que a classe anotada é responsável por receber e responder requisições web.

## 2.9. O que é injeção de dependência

Para ser possível compreender melhor a **injeção de dependência** é necessário entender o que seria a **Inversão de controle**; é uma aplicação concreta da **Inversão de controle (IoC)**.

**O que é inversão de controle?**

É um princípio da programação orientada a objetos, onde se afirma que as dependências **devem ser invertidas**. No caso, seria o 'D' dos princípios **SOLID**. 

**Dependa de abstrações e não de implementações concretas.**

Ao invés de uma classe depender diretamente de outras classes ele deve depender de uma abstração
que defina um contrato.

Os módulos de alto nível **não dependam** diretamente dos detalhes de implementação de módulos de **baixo nível**.

Em outras palavras, os módulos devem depender de conceitos, ou seja, abstrações (_Interfaces_), independentemente de como essas _interfaces_ funcionam. 

| **Importante**: Inversão de dependência é um princípio (Conceito) e a Injeção de dependência é um 
padrão de projeto (‘Design’ Pattern).

**O que é a injeção de dependência?**

É a implementação concreta da Inversão de dependência. Ela permite que as dependências sejam injetadas numa classe por meio de construtores, métodos ou propriedades ao invés de serem criadas internamente na classe. Isso torna o código mais flexível e permite que diferentes implementações das dependências sejam injetadas no mesmo código sem que ele precise ser alterado. 

Ou seja, ao invés da classe depender diretamente de outra classe, a dependência é injetada na classe por meio de constructor, métodos ou propriedades. 

## 2.10. Conhecendo o IoC Container do Spring 

Uma das funcionalidades mais importantes de todo o ecossistema Spring, é o **Spring IoC Container** que é exatamente 
a implementação de Injeção de dependência do ‘Framework’ também é conhecido como Spring Context.

Dessa forma, a responsabilidade de instânciar, configurar e de injetar as dependências é desse container.

**O que é um Bean?**

É o nome dado por objetos gerenciados pelo container do spring.

**@component** Ao usar essa anotação, informamos que essa classe será um componente(bean) e deverá ser gerenciado pelo
‘framework’. É uma das formas de definir um Bean do Spring. 

Na inicialização da aplicação o Spring vai escanear (ComponentScan) as classes do projeto que são componentes spring, vai 
instânciar e vai jogar dentro do seu container. 

## 2.13. Usando o @Configuration e @Bean para definir beans

Existem alguns momentos em que a construção (instanciação do bean) requerem alguma configuração personalizada, dessa forma 
não seria o spring que iria criar, seria o desenvolvedor.

**@configuration**: É também um componente Spring, mas com um objetivo de servir para definições de beans.
**@Bean**: Indica que o método anotado vai instânciar, configurar e inicializar um novo objeto que será gerenciado pelo container Spring.

obs: Dentro do IoC Container, por padrão o bean será nomeado como 
"NotificadorEmail", que é exatamente o nome do método de definição do Bean.

## 2.14. Conhecendo os ponto de injeção e a anotação @Autowired

É onde podemos injetar os objetos dentro dos beans.

@Autowired -> Informamos para o Spring qual ponto de injeção ele vai usar.

- construtor: `public AtivacaoClienteService(Notificador notificador) { ... }`
- setter: Por um método de atribuição `@Autowired public void seNotificador(Notificador notificador) { ... }`
- atributo/variável de instância -> `@Autowired private Notificador notificador;`. Mesmo que o atributo seja privado, o ‘framework’ consegue atribuir nele.

O ideal é usar via construtor, pois deixa muito claro quais são as dependências obrigatórias daquela classe.