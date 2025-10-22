package br.com.financeiro.xpto.desafio_xpto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelatorioSaldoClienteDTO {

    private String nomeCliente;
    private LocalDate clienteDesde;
    private String enderecoCompleto;
    private long movimentacoesCredito;
    private long movimentacoesDebito;
    private long totalMovimentacoes;
    private BigDecimal valorPagoMovimentacoes;
    private BigDecimal saldoInicial;
    private BigDecimal saldoAtual;
}
