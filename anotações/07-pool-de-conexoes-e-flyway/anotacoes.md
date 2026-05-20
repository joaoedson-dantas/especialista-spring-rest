# Pool de conexões e Flyway

## 7.1. Entendendo o funcionamento de um pool de conexões

### Aplicação web **SEM** pool de conexões. 

[Requsição 1] -> [api] -> [Banco de Dados]

Qualquer interação que essa aplicação precisar fazer ao banco de dados ela vai criar uma conexão. Após a API conseguir 
o que queria a conexão é encerrada, já que a requisição foi atendida. 

**A cada requisição cria-se uma conexão com o banco de dados.** Isso vai acontecer com várias requisições em ms.

Imagine que a API receba 6 requisições ao mesmo tempo, no exato ms. Vai ser necessário criar seis conexões ao banco de dados.

### Aplicação web **COM** pool de conexões. 

**Pool de conexões** é um componente de ‘software’ que mantém um conjunto de conexões com o banco de dados para reutilização
numa aplicação.

Ou seja, uma mesma conexão que esteja nesse grupo, será usada diversas vezes em momentos diferentes do dia. **Isso reduz 
o tempo gasto na abertura e fechamento das conexões.** 

A idea é criar um Pool de conexão (Conjunto), onde essas requisições serão reutilizadas em todas as requisições que aplicação web receber.

| Configuramos assim: informamos o quanto de conexão ativa queremos que a aplicação inicie.

**Idle:** (Ocioso) Estado em que a conexão fica quando não está sendo usada. 
  - Ter conexão ociosa é bom pq a qualquer momento podemos precisar delas.


**Imagina que bate 10 requisições a mesmo tempo**

O Pool vai usar as 6 conexões Idle que ele tinha, ele vai ver que não é o suficiente, se o pool estiver configurado 
para criar no máximo 8 conexões (Porque tem o mínimo(4) e a máxima(8)), ele vai atender as 8 requisições ao mesmo tempo, e
duas requisições vai ficar na fila, aguardando a conexão liberar.

| Obs: Não é a requisição que vai ficar na fila, é um processo dentro da aplicação web que na hora de pegar a requisição 9, o pool vai
informar que só pode atender 8, pede para aguardar alguém liberar a conexão. 

| Obs: Geralmente, após as requisições serem atendidas, o Pool não fecha as conexões, existe uma configuração de um tempo máximo que as
| conexões ociosas excedentes devem ser mantidas no Pool. 


O principal benefício é que reduzira o tempo em que o usuário e o consumidor da API terá para ser respondido numa requisição.

## 7.2. Conhecendo o Hikari a solução padrão de pool de conexões no Spring Boot

No Spring já existe um Pool de conexão previamente criado, inicialmente ele cria de cara 10 conexões para serem reutilizadas; 

**O próprio Spring Boot já configurou um Pool** - No pom.xml, temos o start-data-jpa que traz consigo o `HikariCP` - CP significa Connection Pool

**Hikari** é a solução do Pool de conexões que Spring adiciona por padrão. 

Para fazer o teste com várias conexões, podemos usar uma ferramenta da ApacheHttpServer, do servidor Apache.

A ferramenta ab (Apache Benchmark) é usada para fazer testes de carga simples em aplicações HTTP.
Ela é muito útil para testar APIs feitas com Java/Spring Boot, por exemplo.

Após instalado, utilize o comando: 

```bash
    ab -n 2000 -c 50 localhost:8080/restaurantes
```

## 7.3. Configurando o pool de conexões do Hikari

O HikariCP é o pool de conexões JDBC padrão do Spring Boot quando você usa Spring Data JPA.
Ele é responsável por gerenciar conexões com o banco de dados de forma eficiente.

O Hikari fica entre o Hibernate/JPA e o banco.

### Configurando um número diferente de conexões

**Configurando um número máximo de conexões:**

```properties
spring.datasource.hikari.maximum-pool-size=5
spring.datasource.hikari.minimum-idle=3
```

| Só você colocar apenas o valor máximo, o mínimo será atribuído o valor do máximo.

**Configurando o tempo de conexão excedente ociosas: Um tempo limite de ociosidade**

```properties
spring.datasource.hikari.idle-timeout=10000
```

| O mínimo é 10s mesmo. 

## 7.5. Flyway ferramenta de versionamento de schemas de banco de dados 

O Flyway é uma ferramenta de versionamento e migração de banco de dados.

Ele resolve um problema muito comum: **como garantir que o banco de dados esteja sincronizado com o código da aplicação.**

Enquanto você desenvolve o sistema, toda a alteração no banco de dados será gerado uma nova versão do db.

**O que temos que fazer quando o Schema de produção inicía vazio?**

Se quisermos usar a versão 1 em prd, bastariamos executar o script de versão 1 em prd. 

| Obs: A evolução do banco de dados é incremental, se eu estiver a iniciar um db do zero e já estiver 100 versões do db
| para eu chegar na versão 100, eu preciso rodar os 100 scripts de forma incremental, se estiver na 98, não precisa rodar tudo novamente, somente a 99 e 100


**OBS:** NUNCA devemos adicionar uma nova tabela, coluna, nunca renomeia uma tabela, nunca faz nada no banco de dados sem criar um script de migração.

O Flyway transforma alterações de banco em:

- arquivos versionados
- rastreáveis
- automáticos
- reproduzíveis

Quando a aplicação sobe:

1. O Flyway verifica quais scripts já foram executados
2. Executa apenas os novos
3. Salva histórico no banco

## 7.6. Adicionando o Flyway no projeto e criando a primeira migração 

**OBS 1:** Lembre-se, antes de habilitar o Flyway, é necessário desabilitar o schema generetion do hibernate.

```properties
# Se estiver usando, geralmente são esses:
# spring.jpa.generate-ddl=true
# spring.jpa.hibernate.ddl-auto=create
```

**OBS 2:** É necessário excluir todas as tabelas já geradas no banco. Desde do início do projeto, o ideal é que usamos o Flyway.

Após isso, devemos adicionar a dependência do Flyway ao projeto.

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-flyway</artifactId>
</dependency>
```

Após isso, é necessário criar uma pasta `db/migration` no projeto. 

O nome do arquivo que ficará dentro dessa pasta deverá ter um padrão. O padrão é: (V001) após isso separa por 2 underline + descrição + .sql
ex: `V001_criacao-inicial.sql`. **IMPORTANTE** a partir do momento que essa migration foi executada, não devemos mais mudar nada no arquivo. 
Ele fica intocável.

Por padrão, o Flyway cria uma tabela chamada `flyway_schema_history` é uma tabela de controle onde ele vai gerir as migrações.

| **Não é uma boa prática adicionar inserts (DML)** instruções de manipulação de dados, deve evitar criar em migrations.

Toda a evolução do db será aplicada em prd, lembre-se.