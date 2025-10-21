package br.com.financeiro.xpto.desafio_xpto.service;

import br.com.financeiro.xpto.desafio_xpto.domain.enums.TipoMovimentacao;
import br.com.financeiro.xpto.desafio_xpto.dto.ClienteRequestDTO;
import br.com.financeiro.xpto.desafio_xpto.entity.*;
import br.com.financeiro.xpto.desafio_xpto.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public Cliente criarNovoCliente(ClienteRequestDTO request){
        Cliente novoCliente;
        if ("PF".equalsIgnoreCase(request.getTipoCliente())) {
            PessoaFisica pf = new PessoaFisica();
            pf.setCpf(request.getCpfCnpj());
            novoCliente = pf;
        } else if ("PJ".equalsIgnoreCase(request.getTipoCliente())) {
            PessoaJuridica pj = new PessoaJuridica();
            pj.setCnpj(request.getCpfCnpj());
            pj.setRazaSocial(request.getRazaoSocial());
            novoCliente = pj;
        }else {
            throw new IllegalArgumentException("Cliente inválido: "+request.getTipoCliente());
        }
        novoCliente.setNome(request.getNome());
        novoCliente.setTelefone(request.getTelefone());
        if (Objects.nonNull(request.getEndereco())) {
            Endereco ende = new Endereco();
            ende.setRua(request.getEndereco().getRua());
            ende.setNumero(request.getEndereco().getNumero());
            ende.setComplemento(request.getEndereco().getComplemento());
            ende.setBairro(request.getEndereco().getBairro());
            ende.setCidade(request.getEndereco().getCidade());
            ende.setUf(request.getEndereco().getUf());
            ende.setCep(request.getEndereco().getCep());

            novoCliente.setEndereco(ende);
        }
        if (Objects.nonNull(request.getContaInicial())) {
            Conta novaConta = new Conta();
            novaConta.setNomeBanco(request.getContaInicial().getNomeBanco());
            novaConta.setAgencia(request.getContaInicial().getAgencia());
            novaConta.setNumeroConta(request.getContaInicial().getNumeroConta());
            novaConta.setAtivo(true);

        Movimentacao movInicial = Movimentacao.builder()
                .descricao("Movimentação Inicial")
                .tipoMovimentacao(TipoMovimentacao.CREDITO)
                .valor(request.getContaInicial().getSaldoInicial())
                .conta(novaConta)
                .build();

        novaConta.getMovimentacoes().add(movInicial);
        novoCliente.getContas().add(novaConta);
        novaConta.setCliente(novoCliente);

        }
        return clienteRepository.save(novoCliente);
    }
}
