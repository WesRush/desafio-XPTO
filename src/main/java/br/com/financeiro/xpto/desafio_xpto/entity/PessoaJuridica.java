package br.com.financeiro.xpto.desafio_xpto.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Entity
@DiscriminatorValue("PJ")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PessoaJuridica extends Cliente {

    @Column(name = "cnpj",length = 18, unique = true)
    @NotEmpty(message = "CNPJ obrigatório EX.: XX.XXX.XXX/YYYY-ZZ")
    private String cnpj;

    @Column(name = "razao_social")
    private String razaSocial;

}
