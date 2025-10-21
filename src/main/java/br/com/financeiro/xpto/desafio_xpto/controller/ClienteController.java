package br.com.financeiro.xpto.desafio_xpto.controller;


import br.com.financeiro.xpto.desafio_xpto.dto.ClienteRequestDTO;
import br.com.financeiro.xpto.desafio_xpto.entity.Cliente;
import br.com.financeiro.xpto.desafio_xpto.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
