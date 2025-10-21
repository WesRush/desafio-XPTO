package br.com.financeiro.xpto.desafio_xpto.controller;


import br.com.financeiro.xpto.desafio_xpto.dto.ClienteRequestDTO;
import br.com.financeiro.xpto.desafio_xpto.dto.RelatorioSaldoClienteDTO;
import br.com.financeiro.xpto.desafio_xpto.entity.Cliente;
import br.com.financeiro.xpto.desafio_xpto.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return ResponseEntity.ok(relatorio);
    }
    @GetMapping
    public ResponseEntity<List<Cliente>> getTodosClientes(){
        List<Cliente> clientes = clienteService.listarTodosClientes();
        return ResponseEntity.ok(clientes);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    }

