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


