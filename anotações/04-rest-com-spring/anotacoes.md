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

## 4.16. Customizando a represetação em XML com Wrapper e anotações do jackson

Por padrão, quando fazemos uma requisição para uma coleção de recurso, onde o seu tipo de Mídia é XML,
ele retorna as tags como `LIST` e `ITEM`.

Para customizar isso, podemos criar uma classe, e no recurso, ao invés de retornar uma lista, vamos retornar 
essa nova classe, essa classe vai se comportar como um modelo da nossa representação, como se fosse um Wrapper. 

`package -> api.model` -> Essa classe representa o modelo da representação de um recurso (camada externa).

```java
// A função dessa classe é empacotar (embrulhar) uma lista de Cozinhas
// Esse JacksonXmlRootElement poderia ter usado @JsonRootElement("cozinhas"). Funcionaria também
@JacksonXmlRootElement(localName = "cozinhas") // Específico para serialização XML
@Data
public class CozinhasXmlWrapper {

    @NonNull // Do lombok, serve para dizer que essa propriedade não pode ser nula,
            // e deve ser passada no construtor gerado a patir do @Data
    @JacksonXmlProperty(localName = "cozinha") // Uma alternativa seria o JsonProperties, funcionaria também
    @JacksonXmlElementWrapper(useWrapping = false) // desabilitando o embrulho.
    private List<Cozinha> cozinhas;
}
```

**Controller**
```java
// Esse método vai responder apenas quando o consumidor solicitar XML
@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
public CozinhasXmlWrapper listarXml() {
    return new CozinhasXmlWrapper(cozinhaRepository.listar());
}
```

## 4.17. Conhecendo os métodos HTTP

Também conhecidos como verbos HTTP, ele possui uma semântica sobre uma operação efetuada sobre um determinado recurso.

> Através do método, informamos ao Servidor qual o tipo de ação que queremos executar em determinado recurso.

**Idempotência**

Algo idempotente signifíca que tem a capacidade de poder ser aplicado mais de uma vez sem que o resultado da 
primeira aplicação se altere. 

> Exemplo: Imagine que você tem uma Maçã que custa 5$, independente de quantas vezes você perguntar o seu valor não muda. 

É tipo quando você salva várias vezes mesmo arquivo, apertando várias vezes o 'CRTL + S'.

**Principais métodos, HTTP**

- **GET:** (GET /cozinhas HTTP/1.1) - Usado para obter a representação de um recurso.
  - Não pode ser usado para modificar o recurso.
  - É um método seguro (Safe Method), não gera efeito colateral e não modifica o recurso
  - Método **idempotente**
- **POST:** (POST /cozinhas HTTP/1.1) - É usado para criar um novo recurso dentro da coleção de recurso. 
  - Envia um corpo na requisição como payload `{ "nome": "Portuguesa" }`
  - Pode gerar efeito colateral caso tenha mesma requisições várias vezes no recurso. (Novas Cozinhas serão criadas)
  - Não é **idempotente** 
  - Não é seguro - Modifica recursos.
- **PUT:** (PUT /cozinhas/11 HTTP/1.1) - Utilizado como forma de atualizar um determinado recurso.
  - Envia um corpo na requisição como payload `{ "nome": "Portuguesa" }` os dados que devem ser atualizado
  - PUT não pode ser usado para uma atualização parcial, deve ser atualizado por completo (O atributo não passado vai ser considerado como vazio ou nulo)
  - É considerado **idempotente** 
  - Não é seguro - Modifica recursos.
- **PATCH:** (PATCH /cozinhas/11 HTTP/1.1) - Utilizado como forma de atualizar um determinado recurso de forma Parcial
  - Envia um corpo na requisição como payload `{ "nome": "Portuguesa" }` os dados que devem ser atualizado apenas do que foi passado
  - Não é seguro - Modifica recursos.
  - É considerado **idempotente** 
- **DELETE:** (DELETE /cozinha/11 HTTP/1.1) - Remove um determinado recurso.
  - Não se passa corpo e não recebe corpo na requisição
  - Não é seguro 
  - É considerado **idempotente**
- **HEAD:** (HEAD /cozinha/11 HTTP/1.1) - É igual ao GET, mas NUNCA retorna um corpo na resposta 
  - Usado apenas para buscar um cabeçalho
  - As vezes o consumidor da API não está interessado no corpo da resposta (Testar se uma URI existe mesmo, se o Media Type é aceite e etc.)
- **OPTIONS:** (OPTIONS /cozinha/11 HTTP/1.1) - Solicitar uma lista de métodos suportados por um recurso.
  - É útil para o cliente saber quais são os métodos disponíveis.

## 4.18. Conhecendo os códigos de status HTTP

https://www.iana.org/assignments/http-status-codes/http-status-codes.xhtml
https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status

Toda a resposta HTTP possui um **STATUS**, seria um tipo de resultado da requisição feita.

### Nível 200 - Indica que a requisição foi bem sucedida

- 200: ok - Requisições executadas com sucesso;
- 201: criado - Requisição foi atendida e foi criado um recurso.
- 204: Sem conteúdo - Processou a requisição com sucesso e não retorna nenhum corpo. Exclusão de um recurso.

### Nível 300 - Indica status de redirecionamento

- 301 - Movido permanentemente
- 302 - Encontrado (O endereço existe, mas foi alterado temporariamente)

### Nível 400 - Indica algum erro por parte do consumidor da API

Quando um servidor recebe uma requisição e antes de executar o que foi solicitado, ele tem a hipótese de verificar 
se todas as informações das requisições estão corretas, coerentes e se o cliente tem permissão de solicitar. 

Caso tenha algo errado, o servidor responde com status do nível 400

- 400: Requisição mal feita (erro de sintaxe, validação, etc.)
- 401: Não autorizado (Requer que o client esteja autenticado)
- 403: Proibido - Servidor entendeu a requisição, mas recusou a executá-la, pois o client não está autorizado.
- 404: Não encontrado - O recurso solicitado não existe.
- 405: Método não permitido - O verbo HTTP informado na requisição não é suportado para o recurso.
- 406: Não aceito - O servidor não pode retornar representação do recurso usando a _MediaType_ informada no cabeçalho `Accept`.

### Nível 500 - Indica algum erro no servidor

- 500: Erro interno no servidor - Quando o desenvolvedor erra no tratamento de _exceptions_ (NullPointer)
- 503: Serviço indisponível - Sobrecarregado

## 4.19. Definindo o status da resposta HTTP com @ResponseStatus

Por padrão o Spring devolve um código status 200 quando a nossa requisição é bem sucedida. 
É possível modificar o stauts utilizando a anotação `@ResponseStatus(HttpStatus.OK)`

## 4.20. Manipulando a resposta HTTP com ResponseEntity

`ReponseEntity` serve para termos um controle mais fino da resposta HTTP, dando a possibilidade de adicionar 
novos _headers_, mudar o status HTTP de uma forma mais progamática.

Retornamos no controller um `ReponseEntity`, que representa uma reposta HTTP.

```java 
@GetMapping("/{cozinhaId}")
public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {
    Cozinha cozinha = cozinhaRepository.buscar(cozinhaId);

    // return ResponseEntity.status(HttpStatus.OK).body(cozinha);
    // return ResponseEntity.ok(cozinha); // atalho para linha de código comentada acima.

    // exemplo de FOUND -> Movido temporariamente para outra URI
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.LOCATION, "http://localhost:8080/cozinhas"); // Informa no cabeçalho location qual a URI do novo lugar
    return ResponseEntity
            .status(HttpStatus.FOUND)
            .headers(headers)
            .build();
}
```

## 4.21. Corrigindo o Status HTTP para resource inexistente

Tratamento para quando um **resource** não existe. 
Caso um recurso único não exista, ou seja, é chamado para um URI mapeada, deve ser tratado usando o protocolo HTTP.

Deve retornar 404 - NOT FOUND

```java
@GetMapping("/{cozinhaId}")
public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {
    Cozinha cozinha = cozinhaRepository.buscar(cozinhaId);

    if (cozinha != null) {
        return ResponseEntity.ok(cozinha);
    }

    return ResponseEntity.notFound().build();
}
```

## 4.22. Status HTTP para collection resource vazia: qual usar?

A boa prática e o correto é deixar o status 200 retornando uma lista vazia. []

## 4.23. Modelando e implementando a inclusão de recursos com POST

POST /cozinhas HTTP/1.1 - Vamos fazer um post numa coleção. \
Content-Type: application/json \
```json
{
    "nome": "Brasileira"
}
```

**Código**

Para ser possível receber o corpo da requisição, é necessário anotar no parâmetro com `@RequestBody` \
O próprio Spring vai instânciar um Objeto e vai fazer o a viculação das propriedades JSON atribuindo para as \ 
propriedades de cozinha.

Quando criamos um recurso é interessante colocar o status 201 - created e retornar a representação do recurso criado no corpo da resposta.

```java
@ResponseStatus(HttpStatus.CREATED)
@PostMapping
public Cozinha adicionar(@RequestBody Cozinha cozinha) {
    return cozinhaRepository.salvar(cozinha);
}
```

## 4.24. Negociando o media type do payload do POST com Content-Type

É possível negociar o conteúdo sobre o que queremos enviar para a API. Podendo utilizar, além do JSON, o XML, por exemplo.
Será informado no _header_ **Content-Type: application/xml**

**Exemplo:**
POST /cozinhas HTTP/1.1 - \
Content-Type: application/xml \
```xml
<cozinha>
    <nome>Brasileira</nome>
</cozinha>
```

## 4.25. Modelando e implementando a atualização de recursos com PUT

Atualização de um recurso. O verbo HTTP utilizado para fazer uma atualização completa de um resource é PUT. 

PUT /cozinhas/{id} HTTP/1.1 \
Content-Type: application/json \
```json
{
  "nome": "Brasileira"
}
```

## 4.26. Modelando e implementando a exclusão de recursos com DELETE

Exclusão de um recurso único. 

DELETE /cozinhas/{id} HTTP/1.1 \

## 4.27. Implementando a camada de domain services (e a importância da linguagem ubíqua)

No **DDD** existe um conceito chamado **Domain Service/ Serviço de domínio** é uma camada, uma operação sem 'estado' \
que realiza uma tarefa específica do domínio (Tarefa de negócio).
- Quando um processo de um domínio não é uma responsabilidade natural de uma entidade, cria-se um serviço de domínio.

Não é interessante a camada de controller ter acesso direto ao repositório para fazer operações que modificam o 'ESTADO' \
da aplicação. (Salvar | Excluir | Atualizar) - Algo que seria persistido no banco de dados.

> **Modificação no estado aplicação (Processo de negócio)** o ideal é criar outra classe para fazer essa operação.

**Ao criar a classe, é interessante pesar na linguagem usada pelas pessoas do negócio. 
Essa linguagem comum, no DDD chamamos linguagem ubíqua** 

**@Service** -> Anotação utilizada para informar que trata-se de uma Classe de serviço que vai ser um component Spring. 


