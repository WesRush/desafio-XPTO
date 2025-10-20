package br.com.financeiro.xpto.desafio_xpto.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Entity
@DiscriminatorValue("PF")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PessoaFisica extends Cliente{

    @Column(name = "cpf", length = 15,unique = true)
    @NotEmpty(message = "CPF obrigatório EX.: 123.456.789-10")
    private String cpf;
}
