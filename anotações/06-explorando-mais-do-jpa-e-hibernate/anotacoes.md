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

É necessário realizar um tratamento na hora da serialização. Podemos simplemente pedir para ignorar, utilizando a anotação @JsonIgnore 