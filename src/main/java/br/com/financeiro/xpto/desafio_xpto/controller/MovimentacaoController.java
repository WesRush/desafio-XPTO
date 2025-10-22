package br.com.financeiro.xpto.desafio_xpto.controller;

import br.com.financeiro.xpto.desafio_xpto.dto.MovimentacaoRequestDTO;
import br.com.financeiro.xpto.desafio_xpto.entity.Movimentacao;
import br.com.financeiro.xpto.desafio_xpto.service.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/contas/{contaId}/movimentacoes")
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;

    @Autowired
    public MovimentacaoController (MovimentacaoService movimentacaoService){
        this.movimentacaoService = movimentacaoService;
    }

    @PostMapping
    public ResponseEntity<Movimentacao> adicionarMovimentacao(
            @PathVariable("contaId") Long contaId,
            @Valid @RequestBody MovimentacaoRequestDTO requestDTO){

        Movimentacao movimentacaoSalva = movimentacaoService.criarMovimentacao(contaId,requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentacaoSalva);
    }

}
