# REST com Spring

## 4.1. O que é REST? 

Antes de entender o que é REST, é necessário entender o conceito de **API**.

**REST (_REpresentational State Transfer_)**

É um modelo arquitetural, é uma especificação que define a forma de comunicação entre componentes de ‘softwares’ na web,
independente da linguagem de programação usada.

> É um estilo arquitetural para desenvolvimento de web-services (web apis).

O intuíto é formalizar um conjunto de melhores práticas e regras para desenvolvimento de **web-apis**, essas melhores 
práticas são chamadas de **CONSTRAINTS**. 

Uma REST API, é uma API que segue essas melhores práticas (CONSTRAINTS).

**Por que REST?**

- Separação entre cliente e servidor 
  - (Flexibilidade) -> O Sistema cliente pode evoluir, independente do sistema provedor e vice-versa.
- Escalabilidade
- Independência de linguagem.
- Mercado

## 4.2. Conhecendo as constraints do REST 

O REST tem o objetivo de formalizar um **conjunto de constraints** que são as melhores práticas. 
Aqui estão as principais regras.

1. **Cliente-servidor**

- Significa que será necessário um cliente (frontend, mobile, apis...)
- As aplicações cliente (front) e servidor (API REST) devem poder evoluir separadamente sem qualquer dependência entre 
elas.
- Um cliente ou servidor pode ser substituído sem interferir em nada: Exemplo: Front feito em Angular substituído por React

2. **Stateless (SEM ESTADO)**

- A aplicação **NÃO DEVE POSSUIR ESTADO**, a requisição feita ao servidor deve conter tudo que for necessário para ser 
  devidamente processada. 
- A API não pode manter uma "sessão" no servidor. Entre uma requisição e outra o servidor não pode conhecer o cliente.
- Para servidor, cada requisição é como se fosse um cliente desconhecido.
- São informações contextuais sobre o cliente que está a usar a API. (Se ele está logado ou não, o que ele fez etc.)

3. **Cache**

- A API pode fazer _caching_ das respostas das requisições. 
- Exemplo: Recurso que retorne uma lista de cidades, quantas vezes essa lista é alterada num dia/mês/ano? 
- Aqui, a API vai dizer que a resposta pode ser **cacheada**, o cliente vai guardar esses dados num "cache" interno. 
- Melhora a escalabilidade e desempenho da aplicação já que será reduzido o número de "_hits_" no servidor.

Obs: Essa _constraint_ informa apenas a possibilidade e quando for útil e necessário. 

4. **Interface Uniforme**

- Conjunto de operações bem definidas do sistema, uma vez definida como a ‘interface’ da api funciona, é necessário seguir 
essa ‘interface’ religiosamente.
- Permite que cada parte evolua de forma independente. 
- Devemos identificar os recursos do sistema usando URIs, padrão do protocolo de comunicação para interagir com a API (HTTP)
- Devemos adicionar 'links' nas respostas apontando para outros recursos, igual uma página de um site. Isso é conhecido como HATEOS
- A resposta de uma requisição deve ser padronizada, incluindo informações sobre como o cliente deve tratar a mensagem.

4. **Sistema em camadas**

- Outros servidores entre o cliente e servidor como uma camada de cache, segurança etc. Essas camadas não devem
afetar as respostas do servidor, o cliente não deve conhecer quantas camadas possui no meio. 

5. **Código sob demanda**

## 4.3. Diferença entre REST e RESTful

É uma diferença conceitual e simples. REST é o estilo arquitetural que possuem as _constraints_, ou seja, é a especificação.
Já o RESTful é uma API construida conforme as _constraints_, em teoria o uma RESTful API segue todas as constraints obrigatórias
religiosamente.


## 4.4. Desenvolvedores de REST APIs puristas e pragmáticas

**Purístas:** Defendem que REST APIs devem seguir fielmente os princípios rest sem exceções.
**Pragmáticos:** Defendem uma abordagem mais prática, desenvolvem também seguindo as _constraints,_ mas estão abertos a exceções.

## 4.5. Conhecendo o protocolo HTTP

Trata-se de um protocolo REQUISIÇÃO/RESPOSTA.

### Composição da requisição 

[MÉTODO] [URI] HTTP/[VERSÃO]

[Cabeçalhos]

[CORPO/PAYLOAD]

```
POST /produtos HTTP/1.1 
Content-Type: application/json
Accept: application/json

{
    "nome": "Notebook i7",
    "preco": 2100.0
}
```

1. **[MÉTODO] **

Ou verbos HTTP, é uma especificação que define um conjunto de métodos que podemos utilizar para fazer requisição http.
Ou seja, o método informa a ação que deseja ser executada.

Ex: `GET, POST, PUT, DELETE ...`

2. **[URI] **

É um caminho que identifica o que queremos dentro do servidor HTTP

3. [Cabeçalhos]

São informações sobre a requisição. Aqui são definidos nomes de chave e valor, que podem ser usados pelo servidor 
para interpretar a requisição e executar a operação.

**Cabeçalhos pŕedefinidos no protocolo Http**

- Content-Type: application/json 
  - Aqui será definido qual o tipo de conteúdo está a ser enviado no corpo da requisição.
  - Nesse caso, estamos a informar que o corpo está a ir no formato JSON. 
- Accept: application/json
  - Define quais tipos de conteúdo são aceitos como uma resposta. 

[CORPO/PAYLOAD]

Ele não é obrigatório e depende do método HTTP utilizado. É no corpo da requisição que enviamos dados do cliente 
para o servidor. 

### Composição da rsposta

[HTTP]/[VERSAO] [STATUS]
[Cabeçalhos] 
[CORPO]

```
HTTP/1.1  201 Created
Content-Type: application/json

{
    "nome": "Notebook i7",
    "preco": 2100.0
}
```

[STATUS] -> Serve para descrever qual foi o resultado do processamento da requisição. O servidor deverá retornar 
um status adequado para cada situação para que cliente ficar a saber o que aconteceu.

## 4.8. Entendendo o que são Recursos REST

**Resources** é qualquer coisa exposta na web, como um documento, uma página, um vídeo, uma imagem e até mesmo um processo de negócio. 
É algo que tem importância o suficiente para ser referenciado como "Uma coisa no ‘software’"

**Um único produto é um recurso** _Singleton Resource_ | Pense em um recurso com uma instãncia de um objeto de um 
determinado tipo. 

**Coleção de produtos é um recurso** _Collection Resource_ 

## 4.9. Identificando recursos REST

**Como identificar um recurso para referênciar ele na web?**

**URI** _Uniform Resource Identifier_

É um conjunto de caracteres cujo objetivo é dar uma espécie de "endereço" nos recursos, de forma não ambígua.

> Quando modelamos os nossos recursos, temos que pensar em URIs para identificá-los.

**URI vs URL**

Uma URL é um tipo de URI. URL significa _"Uniform Resource Locale - Localizador de recurso uniforme."_  
A URL informa a localização do recurso, onde o recurso está disponível e qual o recurso para chegar até ele.

**Exemplo:**

> `listarProdutos` -> Não é uma boa prática listar dessa forma. A URI deve se referênciar a alguma coisa, ou seja, 
um substantivo e não um verbo ou uma ação. Porque coisas possuem propriedades e verbos não possuem.
> `produtos` -> Seguindo as boas práticas, o ideal é identificar esse recurso como /produtos

Ou seja, não identificamos a ação na URI. A ação será variáda usando apenas os métodos HTTP.

**Exemplo de identificação de recurso único:** `/produtos/{codigo}` 

> OBS: O ideal é usar os nomes sempre no PLURAL, mesmo que sejá um recurso único.

## 4.12. Representação de recursos e content negotiation

**O que uma requisição feita numa URL de um recurso deve retornar?**

Representações de recursos: É um código que descreve o estado atual do recurso.

A representação de um recurso não é o próprio recurso:

- JSON 
- XML 
- JPG

O _cliente_ da API pode especificar na requisição qual formato consegue interpretar.
Essa informação é adicionada no cabeçalho da requisição `Accept:`

Requisição: 
```txt
GET /produtos HTTP/1.1
Accept: application/json
```

Ou seja, essa indicação de qual formato de representação de um mesmo recurso deve ser retornado 
é o que chamamos de `content negotiation`, porque o `client` estaria a negociar com o servidor.

## 4.13. Implementando content negotiation para retornar JSON ou XML 

**Adicionando o suporte a XML**

É necessário adicionar uma nova dependência, a FASTERXML

```xml
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
</dependency>
```

### @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)

Informa que o método só produz um formato específico de conteúdo. 

## 4.14. Consultando Singleton Resource com GET e @PathVariable

Buscando um recurso específico por ID. A ideia é que possamos passar na URI /{id} -> Isso significa que 
estamos a buscar um único recurso.

**Implementação**

Para realizar uma consulta, usamos o método HTTP GET -> 

```java
    // {cozinhaId} é uma variável, chamada placeholder, onde será feito o binding com o parâmetro do método. 
    // Para que esse mapeamento seja feito com sucesso, preciso especificar com o @PathVariable 
    @GetMapping("/{cozinhaId}")
    public Cozinha buscar(@PathVariable Long cozinhaId) {
        return cozinhaRepository.buscar(cozinhaId);
    }
```

## 4.15. Customizando as represetações XML e JSON

A classe de neǵocio, que representam as entidades da aplicação (model/entity), não deve ser utilizada como modelo 
de representação de recurso, na perspectiva de uma REST_API.

E as vezes é necessário customizar a representação.

- @JsonProperty("titulo") -> Não mudamos nome da propriedade, mas apenas a representação. 

Pode ocorrer situações onde temos propriedades no modelo de domínio, onde não queremos que apareça na representação do recurso. 

- @JsonIgnore -> Ignora a propriedade anotada na hora da serialização (Gerar a representação)

Customizar no XML o nome do elemento principal, exemplo, Cozinha 

- @JsonRootName("cozinha") // -> Nome da raiz que será retornada no XML. 