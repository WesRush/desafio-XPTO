package br.com.financeiro.xpto.desafio_xpto.service;

import br.com.financeiro.xpto.desafio_xpto.domain.enums.TipoMovimentacao;
import br.com.financeiro.xpto.desafio_xpto.dto.MovimentacaoRequestDTO;
import br.com.financeiro.xpto.desafio_xpto.entity.Conta;
import br.com.financeiro.xpto.desafio_xpto.entity.Movimentacao;
import br.com.financeiro.xpto.desafio_xpto.repository.ContaRepository;
import br.com.financeiro.xpto.desafio_xpto.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final ContaRepository contaRepository;

    @Autowired
    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository, ContaRepository contaRepository){
        this.movimentacaoRepository = movimentacaoRepository;
        this.contaRepository = contaRepository;
    }
    private BigDecimal calcularSaldoAtual(Conta conta){
        List<Movimentacao> movimentacoesConta = movimentacaoRepository.findByContaId(conta.getId());

        BigDecimal saldo = BigDecimal.ZERO;
        for (Movimentacao mov: movimentacoesConta){
            if (mov.getTipoMovimentacao() == TipoMovimentacao.CREDITO){
                saldo = saldo.add(mov.getValor());
            } else if (mov.getTipoMovimentacao() == TipoMovimentacao.DEBITO) {
                saldo = saldo.subtract(mov.getValor());
            }
        }
        return saldo;
    }

    @Transactional
    public Movimentacao criarMovimentacao(Long contaId, MovimentacaoRequestDTO requestDTO){
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada com ID: "+ contaId));
        if (requestDTO.getTipoMovimentacao()== TipoMovimentacao.DEBITO){
            BigDecimal saldoAtual = calcularSaldoAtual(conta);
            if (saldoAtual.compareTo(requestDTO.getValor()) < 0){ // saldoAtual < valor débito
                throw new RuntimeException("Saldo insuficiente na conta "+ contaId+
                        ". Saldo atual: "+ saldoAtual+
                        ", Valor do débito: "+ requestDTO.getValor());
            }
        }
        Movimentacao novaMovimentacao = Movimentacao.builder()
                .descricao(requestDTO.getDescricao())
                .valor(requestDTO.getValor())
                .tipoMovimentacao(requestDTO.getTipoMovimentacao())
                .conta(conta)
                .build();

        return movimentacaoRepository.save(novaMovimentacao);
    }

}
