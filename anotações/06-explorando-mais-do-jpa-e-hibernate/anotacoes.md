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

É necessário realizar um tratamento na hora da serialização. Podemos simplemente pedir para ignorar, utilizando a anotação @JsonIgnore.

**@JsonIgnore**: Quando o Spring for serializar, deve ignorar a propriedade anotada.

**OBS** Esse mapeamento não significa que foi feito para alterar a representação do recurso.
Muitas vezes, esse mapeammento servirá na lógica de negócio.

##  6.2 - Mapeando relacionamento muitos-para-muitos com @ManyToMany

Nesse caso, a ideia seria, muitos restaurantes possuem muitas formas
de pagamentos. Muitos para muitos. Geramente isso vem de uma configuração 
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

- Endereço, cep, longradouro. 

**Objetos imputidos** São componentes de uma entidade cujas propriedades são copiadas para tabela da entidade.  

**@Embeddable:** Apesar de ser uma classe, não vai ser uma entidade, ela vai ser uma classe incorporável, vai ter a capacidade de ser incorporada em uma entidade. 

Todas as propriedades dessa classe são refletidos na tabela da entidade que incorpora essa classe. 

**@Embedded** // Indica que essa propriedade é uma classe do tipo incorporado. Ou seja, é uma parte da entidade Restaurante.