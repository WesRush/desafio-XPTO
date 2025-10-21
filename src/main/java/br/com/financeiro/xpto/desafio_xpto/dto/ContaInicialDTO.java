package br.com.financeiro.xpto.desafio_xpto.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ContaInicialDTO {
    private String nomeBanco;
    private String agencia;
    private String numeroConta;
    private BigDecimal saldoInicial;
}
