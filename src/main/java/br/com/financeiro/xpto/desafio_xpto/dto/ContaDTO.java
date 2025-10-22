package br.com.financeiro.xpto.desafio_xpto.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class ContaDTO {

    private Long id;

    @NotEmpty(message = "Nome do banco é obrigatório.")
    private String nomeBanco;

    @NotEmpty(message = "Agência é obrigatória.")
    private String agencia;

    @NotEmpty(message = "Número da conta é obrigatório.")
    private String numeroConta;
    private boolean ativo;


    private Long clienteId;
}
