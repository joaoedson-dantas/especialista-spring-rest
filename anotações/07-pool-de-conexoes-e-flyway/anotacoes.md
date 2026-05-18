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