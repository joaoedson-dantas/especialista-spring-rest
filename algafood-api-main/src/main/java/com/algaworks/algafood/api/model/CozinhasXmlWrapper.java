package com.algaworks.algafood.api.model;


import com.algaworks.algafood.domain.model.Cozinha;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

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
