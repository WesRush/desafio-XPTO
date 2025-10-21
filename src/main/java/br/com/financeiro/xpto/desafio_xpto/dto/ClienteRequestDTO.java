package br.com.financeiro.xpto.desafio_xpto.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClienteRequestDTO {

    private String nome;
    private String telefone;
    private String tipoCliente;
    private String cpfCnpj;
    private String razaoSocial;
    private EnderecoDTO endereco;
    private ContaInicialDTO contaInicial;
}
