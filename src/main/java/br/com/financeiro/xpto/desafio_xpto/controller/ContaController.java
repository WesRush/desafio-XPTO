package br.com.financeiro.xpto.desafio_xpto.controller;

import br.com.financeiro.xpto.desafio_xpto.dto.ContaDTO;
import br.com.financeiro.xpto.desafio_xpto.entity.Conta;
import br.com.financeiro.xpto.desafio_xpto.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ContaController {

    private final ContaService contaService;

    @Autowired
    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping("/api/clientes/{clienteId}/contas")
    public ResponseEntity<ContaDTO> criarConta(@PathVariable Long clienteId, @Valid @RequestBody ContaDTO contaDTO) {
        Conta novaConta = contaService.criarConta(clienteId, contaDTO);
        ContaDTO dtoResposta = mapToDTO(novaConta);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoResposta);
    }

    @GetMapping("/api/contas/{id}")
    public ResponseEntity<ContaDTO> getContaPorId(@PathVariable Long id) {
        Conta conta = contaService.buscarContaAtivaPorId(id);
        ContaDTO dtoResposta = mapToDTO(conta);
        return ResponseEntity.ok(dtoResposta);
    }

    @GetMapping("/api/clientes/{clienteId}/contas")
    public ResponseEntity<List<ContaDTO>> getContasPorCliente(@PathVariable Long clienteId) {
        List<Conta> contas = contaService.listarContasAtivasPorCliente(clienteId);
        List<ContaDTO> dtosResposta = contas.stream().map(this::mapToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtosResposta);
    }

    @PutMapping("/api/contas/{id}")
    public ResponseEntity<ContaDTO> atualizarConta(@PathVariable Long id, @Valid @RequestBody ContaDTO contaDTO) {
        Conta contaAtualizada = contaService.atualizarConta(id, contaDTO);
        ContaDTO dtoResposta = mapToDTO(contaAtualizada);
        return ResponseEntity.ok(dtoResposta);
    }

    @DeleteMapping("/api/contas/{id}")
    public ResponseEntity<Void> deletarConta(@PathVariable Long id) {
        contaService.deletarContaLogicamente(id);
        return ResponseEntity.noContent().build();
    }
    private ContaDTO mapToDTO(Conta conta) {
        ContaDTO dto = new ContaDTO();
        dto.setId(conta.getId());
        dto.setNomeBanco(conta.getNomeBanco());
        dto.setAgencia(conta.getAgencia());
        dto.setNumeroConta(conta.getNumeroConta());
        dto.setAtivo(conta.isAtivo());
        if (conta.getCliente() != null) {
            dto.setClienteId(conta.getCliente().getId());
        }
        return dto;
    }

}
