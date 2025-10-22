package br.com.financeiro.xpto.desafio_xpto.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClienteUpdateDTO {
    private String nome;
    private String telefone;
    private EnderecoDTO endereco;


}
