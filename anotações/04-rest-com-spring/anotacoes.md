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