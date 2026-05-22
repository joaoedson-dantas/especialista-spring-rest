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

## 7.7. Evoluindo o banco de dados com novas migrações

Devemos ir a aplicar as migrações a medida que vamos a precisar das tabelas. 

| Cuidado ao criar um arquivo de migration rodando a aplicação com DevTools. Dessa forma o Flyway vai executar a migration
antes mesmo do script ser adicionado. 

Caso isos aconteça não será possível alterar o arquivo. Vai dar um erro de `validate Falid: Migration checksum mismatch for migration version V002`

O Flyway cria uma coluna chamada `checksum`, ele vai pegar o conteúdo do arquivo de migração e vai gerar um número aleatório.
Esse número serve para verficar a integridade do arquivo. 

## 7.8. Criando migrações complexas com remanejamento de dados

Como vc faria caso já tivesse criado uma tabela com dados, por exemplo, uma tabela que guarda **nome_cidade** e **nome_estado** 
e já tem dados cadastrados, já está a ser utilizada, mas agora a missão é criar uma tabela chamada Estado e remanejar esse dado.

Podemos utilizar um script único que já faça esse processo. Ou seja, podemos utilizar as 
migrations para fazer INSERT, UPDATE, desde que seja para remanejamento de dados, de uma tabela para outra.

Exemplo: 

````sql
## 1 - Criar a nova tabela

CREATE TABLE estado (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(80) NOT NULL,

    PRIMARY KEY (id)
) engine=InnoDB default charset=utf8mb4;

## 2 - Transferir dados da tabela cidade para tabela estado - O resultado do SELECT iremos jogar para dentro do insert
INSERT INTO estado (nome)
SELECT DISTINCT c.nome_estado
FROM cidade c;

## 3 - Alterar a tabela de cidade para adicionar a coluna estado_id (Aqui vai ser a chave estrangeira) - Para fazer referência a Estado
ALTER TABLE cidade ADD COLUMN estado_id BIGINT NOT NULL;

## Obs: Como está not null, vai ficar o valor 0 em todos campo de estado_id, por conta disso, não é possível criar a foreign key
## foreign key: A chave estrangeira, a referência de estado_id com a tabela estado. Não tem como fazer referência com id 0;

## 4 - Realizar uma atualização na tabela de cidade, atribuindo o valor correto para id_cidade

UPDATE cidade c SET c.estado_id = (SELECT e.id FROM estado e WHERE e.nome = c.nome_estado);

## 5 - Adicionar o foreign key - Agora conseguimos adicionar a foreign key apontando para tabela estado.
ALTER TABLE cidade ADD CONSTRAINT fk_cidade_estado FOREIGN KEY (estado_id) REFERENCES estado (id);

## 6 - Apagar a coluna nome_estado
ALTER TABLE cidade DROP COLUMN nome_estado;

## 7 - Renomear a coluna nome_cidade
ALTER TABLE cidade CHANGE cidade nome VARCHAR(80) NOT NULL;
````

## 7.9. Criando migrações a partir de DDL gerado por schema generation

Para ter essa "facilidade" no `application.properties` devemos adicionar duas propriedades.
O objetivo é gerar o script via hibernate, mas sem executar ele.
Vamos usar o recurso de criar o DDL a partir do mapeamento do objeto relacional

````properties
spring.jpa.properties.javax.persistence.schema-generation.scripts.action=create
spring.jpa.properties.javax.persistence.schema-generation.scripts.create-target=src/main/resources/ddl.sql
````

Obs: Comente ou remova essas duas propriedades logo após a criação do DDL. Só precisamos disso para 
criar o arquivo uma única vez.

## 7.10. Adicionando dados de testes com callback do Flyway

Para criar uma massa inicial (Muito útil em desenvolvimento e para teste) esse arquivo vai ser chamado em um 
callback do Flyway. 

Quando o Flyway roda existem várias fases que ele vai passando e uma das fases se chama **afterMigrate** que será acionado
após a finalização da execução de todos os scripts de migração.

| Utilize o **insert ignore** do MySQL para ele não ficar a tentar adicionar novamente a cada reload da aplicação.

Ex: 

```sql
insert ignore into cozinha (id, nome) values (4, 'Brasileira');
```

Outra opção é **deletando os dados de todas as tabelas** antes de fazer o insert:

1. Desabilita a checagem de foreign key do mysql 
   1.1 `set foreign_key_checks = 0`
2. Após isso, deleta todos os dados das tabelas
   2.1 `delete from cidade`
3. Habilita novamente a checagem de foreign key do mysql
   1.1 `set foreign_key_checks = 1`
4. Altera as tabelas para zerar o auto-incremento 
   4.1 `alter table cidade auto_increment = 1`
5. Realizar os inserts

| **Existe um problema** na hora de colocar em produção o afterMigrate será executado em todos os ambientes `prd, hom, des`

Devemos criar dentro da pasta `db` vamos criar uma pasta chamada `testData` aqui ficará a massa de dados para que seja 
possível testar a aplicação.

Após isso, adicionamos uma nova propriedade ao `application.properties`.

````properties
# Indica para o flyway onde vão ficar as migrações e o script de callback
spring.flyway.locations=classpath:db/migration,classpath:db/testdata
````

| Feito isso, a criação de `application.properties` é de acordo com o profiles, pode ter um de **prd, hom e des**
Basicamente em produção eu poderia apenas remover a propriedade ou até mesmo não referenciar no locations.

## 7.11. Reparando migrações com erros

Quando uma migração falha, é necessário intervir de alguma forma no Flyway, para dizer que vou rodar novamente. 
O que podemos fazer seria um DELETE da linha com falha da tabela ``flyway_schema_history``.

**Importante:** Se possuir uma migração com muitas instruções e ocorre uma falha no meio do proceso, esse reparo não é 
só excluir a versão e rodar novamente. É necessário analisar até onde foi executado, desfazer o que foi feito e só então 
excluimos a migração com falha e roda novamente a corrida. 

| Por isso, é sempre bom fazer um backup do banco antes de modificar ou fazer algum script.sql antes. 

### Usando o Maven para reparar. 

Via terminal e o maven, podemos pedir para o Flyway realizar o reparo.

````bash
./mvnw flyway:repair
````

Vai falhar pq estamos a usar diretamente a ferramenta Flyway, e o ela não tem nada haver com o Spring. Por conta disso
é necessário se autenticar ao banco de dados.

**Criar arquivo de propriedades do Flyway**

````properties
flyway.url=jdbc:mysql://localhost:3306/algafood?createDatabaseIfNotExist=true&serverTimezone=UTC
flyway.user=root
flyway.password=mysql
````

Agora podemos chamar novamente, passando as propriedades de configuração.

````bash
./mvnw flyway:repair -Dflyway.configFiles=src/main/resources/flyway
````

Ele vai fazer o repair.