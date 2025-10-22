package br.com.financeiro.xpto.desafio_xpto.controller;


import br.com.financeiro.xpto.desafio_xpto.dto.ClienteRequestDTO;
import br.com.financeiro.xpto.desafio_xpto.dto.ClienteUpdateDTO;
import br.com.financeiro.xpto.desafio_xpto.dto.RelatorioSaldoClienteDTO;
import br.com.financeiro.xpto.desafio_xpto.entity.Cliente;
import br.com.financeiro.xpto.desafio_xpto.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> cadastrarNovoCliente(@RequestBody ClienteRequestDTO request){
        Cliente clienteSalvo = clienteService.criarNovoCliente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClientePorId(@PathVariable("id") Long clienteId){
       Cliente cliente = clienteService.buscarClientePorId(clienteId);
       return ResponseEntity.ok(cliente);
    }
    @GetMapping("/{id}/relatorio-saldo")
    public ResponseEntity<RelatorioSaldoClienteDTO> getRelatorioSaldo(@PathVariable("id") Long clienteId) {
        RelatorioSaldoClienteDTO relatorio = clienteService.gerarRelatorioSaldoCliente(clienteId);

        Locale localeBR = new Locale("pt", "BR");
        NumberFormat formatadorMoeda = NumberFormat.getCurrencyInstance(localeBR);
        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        StringBuilder relatorioFormatado = new StringBuilder();
        relatorioFormatado.append("\n=============================================\n");
        relatorioFormatado.append("         RELATÓRIO DE SALDO CLIENTE\n");
        relatorioFormatado.append("---------------------------------------------\n");
        relatorioFormatado.append(String.format("Cliente: %s - Cliente desde: %s\n",
                relatorio.getNomeCliente(),
                relatorio.getClienteDesde().format(formatadorData)));
        relatorioFormatado.append(String.format("Endereço: %s\n",
                relatorio.getEnderecoCompleto()));
        relatorioFormatado.append("---------------------------------------------\n");
        relatorioFormatado.append(String.format("Movimentações de crédito: %d\n",
                relatorio.getMovimentacoesCredito()));
        relatorioFormatado.append(String.format("Movimentações de débito: %d\n",
                relatorio.getMovimentacoesDebito()));
        relatorioFormatado.append(String.format("Total de movimentações (pagas): %d\n",
                relatorio.getTotalMovimentacoes()));
        relatorioFormatado.append(String.format("Valor pago pelas movimentações: %s\n",
                formatadorMoeda.format(relatorio.getValorPagoMovimentacoes())));
        relatorioFormatado.append("---------------------------------------------\n");
        relatorioFormatado.append(String.format("Saldo inicial: %s\n",
                formatadorMoeda.format(relatorio.getSaldoInicial())));
        relatorioFormatado.append(String.format("Saldo atual: %s\n",
                formatadorMoeda.format(relatorio.getSaldoAtual())));
        relatorioFormatado.append("=============================================\n");

        System.out.println(relatorioFormatado.toString());

        return ResponseEntity.ok(relatorio);
    }
    @GetMapping
    public ResponseEntity<List<Cliente>> getTodosClientes(){
        List<Cliente> clientes = clienteService.listarTodosClientes();
        return ResponseEntity.ok(clientes);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atulizarCliente(
            @PathVariable("id") Long clienteId,
            @RequestBody ClienteUpdateDTO atulizacaoDados){
        Cliente clienteAtualizado = clienteService.atualizarCliente(clienteId, atulizacaoDados);
                return ResponseEntity.ok(clienteAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(
            @PathVariable("id") Long clienteId){
        clienteService.deletarCliente(clienteId);
        return ResponseEntity.noContent().build();
    }


    }

