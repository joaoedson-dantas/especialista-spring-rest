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

## 2.15. Dependência Opcional com @Autowired

Por padrão, as dependências são obrigatórias, mas podemos informar que a mesma
pode ser opcional 

@Autowired(required = false) -> Informa para o Spring que não uma dependência obrigatória.

## 2.16. Ambiguidade de beans e injeção de lista de beans

Quando existem dois beans do mesmo tipo, o Spring precisa saber qual desses beans vai ser injetado. 

No exemplo, criamos dois notificadores, o NotificadorEmail e NotificadoSms. 

Exemplo de error: 

```bash
Field notificador in com.algaworks.algafood.di.service.AtivacaoClienteService required a single bean, but 2 were found:
	- notificadorSMS: defined in file [/home/joaoedson/dev/java/algaworks/especialista-spring-rest/algafood-api-di/target/classes/com/algaworks/algafood/di/notificacao/NotificadorSMS.class]
	- notificadorEmail: defined by method 'notificadorEmail' in class path resource [com/algaworks/algafood/di/NotificacaoConfig.class]
```

Observe que, **_required a single bean, but 2 were found:_**

Esse é um clássico problema de ambiguidade, de imprecisão, o container do Spring não consegue definir qual que precisa ser injetado
para solucionar esse problema precisamos fazer uma desambiguação.

### Formas de desambiguação

1. Consumidor aceitar múltiplos beans: Já que existe mais de um bean no container do Spring, o consumidor pode passar a receber uma lista de beans. 
- Exemplo: `@Autowired private List<Notificador> notificador`.

## 2.17. Desambiguação de beans com o @Primary

Action: Consider marking one of the beans as @Primary

Dentro da definição do Bean, no caso, o NotificadorSms e o NotificadorEmail informar para o Spring que esse bean 
vai ter uma maior prioridade utilizando anotação @Primary

Exemplo: 

```java
    @Component // Informa que essa classe vai ser um componente Spring (bean) onde vai passar a ser gerenciado pelo Framework
    @Primary // Informa que esse bean vai ter prioridade e vai ser utilizado para fazer desambiguação
    public class NotificadorSMS implements Notificador {}
```

## 2.18. Desambiguação de beans com o @Qualifier

Action: or using @Qualifier to identify the bean that should be consumed

Essa anotação serve para qualificar o componente do spring e nessa qualificação, colocamos um identificador,
`@Qualifier("sms")`, após isso, onde injetamos o Notificador, é possível usar a anotação @Qulifier("seuIdentificador")

Aqui, basicamente informamos para o Spring utilizar um Bean que tem um qualificador chamado sms.

O qualificador, é ideal colocar como nível de prioridade. Como URGENTE ou NORMAL, por exemplo. 


## 2.19. Desambiguação de beans com anotações customizada

Um problema de utilizar o @Qualifier é que ele recebe um `String`, e uma `String` não é testada em tempo de compilação 
somente de execução. 

Com isso, podemos criar uma anotação de forma customizada e, nessa anotação, é necessário anotar 
com @Qualifier para que ele psossa 'herdar' a função. 

```java
@Qualifier 
@Retention(RetentionPolicy.RUNTIME) // Informa quanto tempo essa anotação (TipoDoNotificador) deve permanecer onde ela foi usada
// Runtime -> Informa que ela pode ser lida em tempo de execução
public @interface TipoDoNotificador {

    // Essa anotação vai receber como parâmetro o nível de urgência
    NivelUrgencia value(); // value -> Valor padrão quando não é passado o valor para anotação
}
```
## 2.20. Mudando o comportamento da aplicação com o Spring Profile

Spring Profiles é uma forma de separar componentes da aplicação, que **serão disponibilizados em certos ambientes.**

É quando temos uma mesma aplicação, mas queremos que essa aplicação se comporte diferente dependendo 
do ambiente que ela está executando. 

## Como informamos o Profile?

Via parâmetro na inicialização da aplicação. 

Ex: banco de dados, em produção e desenvolvimento.

Primeiro fazemos utilizamos uma anotação @Profile, para informar para o Spring que o componente anotado
vai ser registrado no container do spring apenas se estiver rodando no ambiente de prod

```java
    @Profile("prod")
    public class NotificadorEmail implements Notificador {}
```

Ou seja, se não estivendo executando no ambiente de produção, o Bean nem chega a ser instânciado, ele 
fica desconhecido pelo container do Spring.

Após realizar as anotação, é necessário passar para o Spring qual o profile que vai ser utilizado.

### Como passar um profile na hora de iniciar uma aplicação?

No arquivo de configuração.

`spring.profiles.active=prod`

**Para passar o profile por linha de comando, faça:**
```Bash
    java -jar projeto.jar - Dspring.profiles.active=prod
```

## 2.21. Criando métodos de callback do ciclo de vida dos beans

Todos os `beans` do spring tem um cíclo de vida.

Ciclo de vida são as fazes desde a existência desse bean, ou seja, surgimento da sua instância, 
até onde ele deixa de existir no container.

Em resumo, esse ciclo de vida aparece em três fases: 

1. Inicialização do Bean
2. Fase de utilização do bean 
3. Fase de destruição

Nos cíclos de vida, podemos implementar métodos de callback em cada fase do cíclo. 

**callback:** São métodos declarados, chamados pelo próprio container quando passa por alguma fase 
do cíclo de vida. 

**Como faze?**

1. Via métodos
```java
   @PostConstruct // Ele vai executar esse método após ser chamado o construtor e após realizar todas as injeções
   public void init() {
   System.out.println("INIT" + notificador);
   }

   @PreDestroy // É chamado quando o bean deixa de existir, ele é chamado um pouco antes.
   public void destroy() {
   System.out.println("DESTROY");
   }
```

2. Chamando via definição do Bean

```java
    
    @Bean(initMethod = "init", destroyMethod = "destroy")
    public AtivacaoClienteService ativacaoClienteService() {
        return new AtivacaoClienteService();
    }
```

## 2.22. Publicando e consumindo eventos customizados

Existe um padrão de projeto chamado **Observer** e esse padrão serve para deixar o 
acoplamento mais baixo entre as classes e manter outros objetos informados.

O Spring implementa esse padrão, chamado _EventHandler_. 

Vai ser emitido um evento para a determinada ação, e vai existir 'ouvintes', ou seja, 
consumidores interessados nesse evento. 

Exemplo: 

```java

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void ativar(Cliente cliente) {
        cliente.ativar();
        
        // Aqui vamos apenas dizer para o container que o cliente está ativo neste momento.
        // [antes] -> Notificava gerando um forte acoplamento
        // notificador.notificar(cliente, "O seu cadastro no sistema está ativo!"); 
        
        // [depois] -> Apenas dizer para o container que o cliente já está ativo 
        // Emitir um sinal, um evento.
        applicationEventPublisher.publishEvent(new ClienteAtivadoEvent()); // passa como parâmetro o evento criado
    }
```

1. **Implementando o evento**
Aqui basta criar a classe que represente o evento 
`public class ClienteAtivadoEvent {}`
2. **Publicar o Evento**
O Spring fornece um objeto que permite publicar eventos
`applicationEventPublisher.publishEvent(new ClienteAtivadoEvent(cliente));`
3. **Implementar o ouvinte ou listener**
```java
@Component
public class NotificacaoService {

    @TipoDoNotificador(NivelUrgencia.NORMAL)
    @Autowired
    private Notificador notificador;

    @EventListener // Informo para o Spring que esse método é um ouvinte de um evento
    public void clienteAtivadoListener(ClienteAtivadoEvent event) {
        System.out.println("Cliente " + event.getCliente().getNome() + " agora está ativo");
        this.notificador.notificar(event.getCliente(), "Seu cadastro no sistema está ativo!");
    }

}
```


