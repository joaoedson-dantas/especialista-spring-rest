# Explorando mais do JPA e Hibernate

## 6.1 Mapeando relacionamento bidirecional com @OneToMany

No caso, será feito o mapeamento onde o Restaurante TEM UMA Cozinha.

Observe que, no diagrama, cozinha não tem acesso ao Restaurante, **isso significa que o mapeamento é unidirecional**.

Vamos transformar esse mapeamento em bidirecional, onde uma Cozinha poderá ter muitos Restaurantes.

**@OneToMany:** Mapeamento de um para muitos, seria o mapeamento inverso ao Muitos para um, ou @ManyToOne

| Um Bizu, sempre que a anotação **terminar com ¨Many¨** significa que será uma coleção.

**mappedBy:** Essa propriedade servirá para indicar qual que é o nome da propriedade inversa, o mesmo nome que vai está em Restaurante, no caso, cozinha. Informamos onde foi mapeado a associação.
¨¨

```java
@OneToMany(mappedBy = "cozinha")
private List<Restaurante> restaurantes = new ArrayList<>(); // Pode ser bom para evitar Null
```

### Quem será o dono da associação? 

O dono será a entidade Restaurante, pois essa que guarda a chave estrangeira.
Além disso, como seria salvo uma lista de restaurante dentro de Cozinha? 


**IMPORTANTE**

Ao usar o mapeamento bidirecional e usar as entidades (Modelo de domínio) como o modelo da API. As representações serão 
serializadas, o problema é quando buscar uma Cozinha, dentro dela vai ter um restaurante e dentro do restaurante vai ter 
uma Cozinha ...

Isso gerara uma dependência circular. Pois o Jackson vai tentar serializar para Json. 

É necessário realizar um tratamento na hora da serialização. Podemos simplesmente pedir para ignorar, utilizando a anotação @JsonIgnore.

**@JsonIgnore**: Quando o Spring for serializar, deve ignorar a propriedade anotada.

**OBS** Esse mapeamento não significa que foi feito para alterar a representação do recurso.
Muitas vezes, esse mapeammento servirá na lógica de negócio.

##  6.2 - Mapeando relacionamento muitos-para-muitos com @ManyToMany

Nesse caso, a ideia seria, muitos restaurantes possuem muitas formas
de pagamentos. Muitos para muitos. Geralmente isso vem de uma configuração 
global da aplicação.

Para isso, criamos uma tabela pivô, será uma tabela intermediária `restaurante_forma_pagamento` vai existir para ser possível criar essa relação de muitos para muitos. 

```java
@ManyToMany // Muitos restaurantes possuem muitas formas de pagamento.
@JoinTable( // Ajuda a costumizar como ficará o nome da tabela intermediaria, assim como as colunas
        name = "restaurante_forma_pagamento", // nome da tabela
        joinColumns = { // Vai definir qual o nome da coluna, da tabela intermediária, que associa a restaurante.
            @JoinColumn(name = "restaurante_id")  // O JoinColumn -> Define o nome da coluna que faz referência a própria classe que estamos mapeando, no caso restaurante
        },
        inverseJoinColumns = {
                @JoinColumn(name = "forma_pagamento_id")
        }
)
private List<FormaPagamento> formasPagamento = new ArrayList<>();
```

## 6.3. Analisando o impacto do relacionamento muitos-para-muitos na REST API

Trazer o relacionamento numa coleção de recursos pode deixar o payload bem grande, a depender da quantidade de informações solicitada. Por exemplo, 
para cada restaurante, iria trazer várias formas de pagamento. 

Mas, seria uma boa se estivesse se tratando de um recurso único.

**Obs:** Para cada relacionamento a mais, o hibernate realiza um nova consulta.

Vários recuros: Representação mais enxuta 
ùnico recurso: Representação mais completa.

## 6.4. Mapeando classes incorporáveis com @Embedded e @Embeddable

Em algumas situações é necessário componentizar as entidades, ou seja, separar
duas ou mais classes que representam alguma coisa no modelo de domínio.

Ex: A classe Restaurante tem um endereço. Geralmente, as classes **embeddable** são componentes reutilizados no projeto. 
Exemplo, um cliente e um fornecedor tem um endereço.

- Endereço, cep, longradouro. 

**Objetos imbutidos ou incorporáveis:** São componentes de uma entidade cujas propriedades são copiadas para tabela da entidade.  

**@Embeddable:** apesar de ser uma classe, não vai ser uma entidade, ela vai ser uma classe incorporável, vai ter a capacidade de ser incorporada numa entidade. 
É uma parte de uma entidade e não uma entidade em sí.

Todas as propriedades dessa classe são refletidos na tabela da entidade que incorpora essa classe. 

**@Embedded** // Indica que essa propriedade é uma classe do tipo incorporado. Ou seja, é uma parte da entidade Restaurante.

## 6.5. Testando e analisando o impacto da incorporação de classe na REST APIs

Na collection resource (Lista) não compensa retornar os objetos incorporáveis, talvez é escusado. 

Temos que pensar no consumir da API, sempre que ele fizer uma request, solicitando essa lista, ele vai querer o endereço tbm? 

Podemos tirar da representação usando a anotação @JsonIgnore.

## 6.6. Mapeando propriedades com @CreationTimestamp e @UpdateTimestamp

**@CreationTimestamp:** A propriedade anotada deve ser atribuído como data e hora atual no momento que ela for salva pela primeira vez
**@UpdateTimestamp:** A propriedade anotada deve ser atribuído como data e hora atual sempre que a entidade for atualizada


OBS: Essa anotação são da implementação do hibernate, não é do JPA, isso significa que ficamos 'presos' ao hibernate. \
se quiser mudar a implementação teremos que ajustar a anotação.

```java
@CreationTimestamp
@Column(nullable = false, columnDefinition = "datetime")
private LocalDateTime dataCadastro; // LocalDateTime: Representa uma data e hora sem fuso horário, sem timestamp

@UpdateTimestamp
@Column(nullable = false, columnDefinition = "datetime")
private LocalDateTime dataAtualizacao;
```

**columnDefinition = "datetime(2)":** Cria uma coluna dataTime sem a precisão dos segundos, 
mas é possível passar por parâmetros a precisão desejada.

## 6.10. Entendendo o Eager Loading

Em muitos casos, quando realizamos uma operação de consulta utilizando o hibernante, dependendo do mapeamento, pode ser
realizado mais de uma consulta. 

**Eager Loading:** Mesmo que você não esteja a usar algumas propriedades, ou até mesmo utilizando o JsonIgnore para não trazer
a propriedade na representação essa consulta será feita justamente por conta do Eager Loading.

| Por padrão, todas as associações ToOne (ManyToOne, OneToOne) usa por padrão a estratégia Eager Loading.

Eager-> Em tradução livre seria "Ansioso", ao contexto seria "Um carregamento ansioso ou antecipado".

Nesse, sentido, toda a vez que uma instância de _Restaurante_ é carregada a partir do banco de dados, ele vai carregar 
as associações a partir do eager loading.

Para uma lista de Restaurante, para cada item o JPA vai buscar a Cozinha relacionada a esse Restaurante, porque é um carregamento ansioso.

Eager quer dizer que no momento que carrega a entidade Restaurante vai carregar junto (Cozinha e Cidade)
**junto** -> Significa apenas que vai carregar junto, independente como, o como fica a parte da implementação. 

Obs: Não estamos falando que o SELECT vai ser um só ou que vão ser mais de um SELECT.

------

Por que o INNER e o LEFT? O hibernate vai usar o inner quando a propriedade tem a definição de (nullable = false)

```java
@JsonIgnore
@JoinColumn(nullable = false) // Aqui o restaurante não existe sem a Cozinha, logo deverá ser um inner 
@ManyToOne
private Cozinha cozinha;

@JsonIgnore
@Embedded // Indica que essa propriedade é uma classe do tipo incorporado. Ou seja, é uma parte da entidade Restaurante.
private Endereco endereco; // Como não temos o nullable, ele deve fazer um Left para ver se tem ou não
```

## 6.11. Entendendo o Lazy Loading

**Lazy** Significa "PREGUIÇOSO" é um carregamento que só vai acontencer quando realmente for necessário, quando estivermos 
a usar aquela **propriedade da associação**. Se não usar, ele simplesmente **não vai fazer o carregamento.** 

_Toda a propriedade que anotação terminar com **"ToMany"**_ como OneToMany ou ManyToMany usa a estratégia por padrão LAZY.

O Lazy é um carregamento por demanda. 

Analisando essa associação:
```java
@JsonIgnore
@ManyToMany // Muitos restaurantes possuem muitas formas de pagamento.
@JoinTable( // Ajuda a costumizar como ficará o nome da tabela intermediaria, assim como as colunas
        name = "restaurante_forma_pagamento", // nome da tabela
        joinColumns = { // Vai definir qual o nome da coluna, da tabela intermediária, que associa a restaurante.
                @JoinColumn(name = "restaurante_id")  // O JoinColumn -> Define o nome da coluna que faz referência a própria classe que estamos mapeando, no caso restaurante
        },
        inverseJoinColumns = {
                @JoinColumn(name = "forma_pagamento_id")
        }
)
private List<FormaPagamento> formasPagamento = new ArrayList<>();
```

## 6.12. Alterando a estratégia de fetching para Eager Loading

Alterando a estratégia de fetching para um carregamento ansioso. Para isso, passamos um parâmetro para anotação de mapeamento
da JPA. 

```java
@JsonIgnore
@ManyToMany(fetch = FetchType.EAGER) // Muitos restaurantes possuem muitas formas de pagamento.
@JoinTable( // Ajuda a costumizar como ficará o nome da tabela intermediaria, assim como as colunas
        name = "restaurante_forma_pagamento", // nome da tabela
        joinColumns = { // Vai definir qual o nome da coluna, da tabela intermediária, que associa a restaurante.
                @JoinColumn(name = "restaurante_id")  // O JoinColumn -> Define o nome da coluna que faz referência a própria classe que estamos mapeando, no caso restaurante
        },
        inverseJoinColumns = {
                @JoinColumn(name = "forma_pagamento_id")
        }
)
private List<FormaPagamento> formasPagamento = new ArrayList<>();
```

## 6.13. Resolvendo o problema do N+1 com fetch join na JPQL

O problema acontece quando a aplicação executa:

- Uma consulta principal 
  - N consultas adicionais para carregar relacionamentos de cada registro retornando

Obs: Isso geralmente ocorre devido ao carregamento `LAZY` de entidades relacionadas.

Devemos sempre analisar e entender se é prejudicial ou não. 

**Exemplo:** 

Imagine duas entidades:

```java

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

public class Pedido {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;
}

@Entity
public class Cliente {
    @Id
    private Long id;
    private String nome;
}
```

Resultado:

- 1 query para pedidos
    - 100 queries para clientes

Total:
101 queries

Daí vem o nome **(N+1)** -> 1 consulta inicial + **N** consultas adicionais

### Por que isso é ruim?

Porque degrada MUITO a performance 

- Mais acesso ao banco 
- mais latência 
- maior uso de conexão 
- pior escalabilidade


### Como resolver? 

Uma das formas de resolver é escrever uma consulta JPQL.

```java
@Query("from Restaurante r join r.cozinha") // busca os restaurantes fazendo o join em Cozinha, aqui vai ser feito um inner join em Cozinha
List<Restaurante> findAll();
```
O pulo do gato é esse `join`. Agora o Hibernate faz uma única query. Podemos utilizar também para duas propriedades usando o fetch.

```java
@Query("from Restaurante r join r.cozinha join fetch r.formasPagamento") // busca os restaurantes fazendo o join em Cozinha, aqui vai ser feito um inner join em Cozinha e formas pagamento
List<Restaurante> findAll();
```

**OBS:** Se um restaurante não tiver nenhuma forma de pagamento associada a ele, esse restaurante não será retornado.
Para resolver isso, temos que usar LEFT JOIN FETCH, no lugar do JOIN FETCH.

```java
@Query("from Restaurante r join r.cozinha LEFT JOIN FETCH r.formasPagamento") // busca os restaurantes fazendo o join em Cozinha, aqui vai ser feito um inner join em Cozinha
List<Restaurante> findAll();
```

**Mas pq precisa do fetch?**

Uma associação **ManyToOne** a JPA faz o `FETCH` automáticamente quando fazemos o JOIN com **ManyToMany** ele não faz 
o fetch automaticamente. Lembre-se que ele precisa ir na tabela intermediária criada por conta do muito para muitos. 

É boa prática colocar o `FETCH` até onde não é necessário, para deixar explícito.