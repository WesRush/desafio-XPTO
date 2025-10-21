package br.com.financeiro.xpto.desafio_xpto.service;

import br.com.financeiro.xpto.desafio_xpto.domain.enums.TipoMovimentacao;
import br.com.financeiro.xpto.desafio_xpto.dto.ClienteRequestDTO;
import br.com.financeiro.xpto.desafio_xpto.dto.RelatorioSaldoClienteDTO;
import br.com.financeiro.xpto.desafio_xpto.entity.*;
import br.com.financeiro.xpto.desafio_xpto.repository.ClienteRepository;
import br.com.financeiro.xpto.desafio_xpto.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final EntityManager entityManager;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository,
                          MovimentacaoRepository movimentacaoRepository,
                          EntityManager entityManager){
        this.clienteRepository = clienteRepository;
        this.movimentacaoRepository = movimentacaoRepository;
        this.entityManager = entityManager;
    }

    /**
     * === MÉTODO 1: CRIAR NOVO CLIENTE ===
     * (Sua lógica refatorada - está perfeita)
     */
    @Transactional
    public Cliente criarNovoCliente(ClienteRequestDTO request){
        Cliente novoCliente = getCliente(request);
        if (Objects.nonNull(request.getContaInicial())) {
            Conta novaConta = new Conta();
            novaConta.setNomeBanco(request.getContaInicial().getNomeBanco());
            novaConta.setAgencia(request.getContaInicial().getAgencia());
            novaConta.setNumeroConta(request.getContaInicial().getNumeroConta());
            novaConta.setAtivo(true);

            BigDecimal saldoInicial = request.getContaInicial().getSaldoInicial();

            if (saldoInicial != null && saldoInicial.compareTo(BigDecimal.ZERO) != 0) {
                TipoMovimentacao tipoMovimentacao;
                String descricao;
                if (saldoInicial.compareTo(BigDecimal.ZERO) > 0) {
                    tipoMovimentacao = TipoMovimentacao.CREDITO;
                    descricao = "Depósito Inicial";
                } else {
                    tipoMovimentacao = TipoMovimentacao.DEBITO;
                    descricao = "Débito Inicial";
                }

                Movimentacao movInicial = Movimentacao.builder()
                        .descricao(descricao)
                        .tipoMovimentacao(tipoMovimentacao)
                        .valor(saldoInicial.abs())
                        .conta(novaConta)
                        .build();
                novaConta.getMovimentacoes().add(movInicial);
            }
            novoCliente.getContas().add(novaConta);
            novaConta.setCliente(novoCliente);
        }
        return clienteRepository.save(novoCliente);
    }
    private static Cliente getCliente(ClienteRequestDTO request) {
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
            throw new IllegalArgumentException("Cliente inválido: "+ request.getTipoCliente());
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
        return novoCliente;
    }

    /**
     * === MÉTODO 2: BUSCAR POR ID ===
     * (Sua lógica original - está perfeita)
     */
    @Transactional(readOnly = true)
    public Cliente buscarClientePorId(Long clienteId){
        Optional<Cliente> clienteOptional = clienteRepository.findById(clienteId);
        if (clienteOptional.isPresent()) {
            return clienteOptional.get();
        }else {
            throw new RuntimeException("Cliente não encontrado com ID: "+clienteId);
        }
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarTodosClientes(){
        return clienteRepository.findAll();
    }
    @Transactional(readOnly = true)
    public RelatorioSaldoClienteDTO gerarRelatorioSaldoCliente(Long clienteId) {

        Cliente cliente = this.buscarClientePorId(clienteId);
        BigDecimal saldoInicial = BigDecimal.ZERO;
        BigDecimal saldoAtual = BigDecimal.ZERO;
        long totalCredito = 0;
        long totalDebito = 0;

        List<Movimentacao> todasMovimentacoesDoBanco = movimentacaoRepository.findAll();

        for (Movimentacao mov : todasMovimentacoesDoBanco) {
            if (mov.getConta() != null && mov.getConta().getCliente() != null && mov.getConta().getCliente().getId().equals(clienteId)) {
                if (mov.getDescricao().contains("Inicial")) {
                    if (mov.getTipoMovimentacao() == TipoMovimentacao.CREDITO) {
                        saldoInicial = saldoInicial.add(mov.getValor());
                        saldoAtual = saldoAtual.add(mov.getValor());
                    } else {
                        saldoInicial = saldoInicial.subtract(mov.getValor());
                        saldoAtual = saldoAtual.subtract(mov.getValor());
                    }
                }
                else if (mov.getTipoMovimentacao() == TipoMovimentacao.CREDITO) {
                    totalCredito++;
                    saldoAtual = saldoAtual.add(mov.getValor());
                }
                else if (mov.getTipoMovimentacao() == TipoMovimentacao.DEBITO) {
                    totalDebito++;
                    saldoAtual = saldoAtual.subtract(mov.getValor());
                }
            }
        }

        long totalMovimentacoesParaTaxa = totalCredito + totalDebito;

        javax.persistence.Query query = entityManager
                .createNativeQuery("SELECT fn_calcular_taxa_xpto(?1)");


        query.setParameter(1, (int) totalMovimentacoesParaTaxa);
        Object result = query.getSingleResult();
        BigDecimal valorPago;
        if (result instanceof BigDecimal) {
            valorPago = (BigDecimal) result;
        } else if (result instanceof Number) {
            valorPago = new BigDecimal(((Number) result).toString());
        } else {
            throw new RuntimeException("Resultado inesperado da função fn_calcular_taxa_xpto: " + result);
        }

        String enderecoStr = "Endereço não cadastrado";
        if (cliente.getEndereco() != null) {
            Endereco end = cliente.getEndereco();
            enderecoStr = String.format("%s, %s, %s, %s, %s, %s, %s",
                    end.getRua(), end.getNumero(),
                    (end.getComplemento() != null ? end.getComplemento() : ""),
                    end.getBairro(), end.getCidade(), end.getUf(), end.getCep());
        }

        return RelatorioSaldoClienteDTO.builder()
                .nomeCliente(cliente.getNome())
                .clienteDesde(cliente.getDataCadastro())
                .enderecoCompleto(enderecoStr)
                .movimentacoesCredito(totalCredito)
                .movimentacoesDebito(totalDebito)
                .totalMovimentacoes(totalMovimentacoesParaTaxa)
                .valorPagoMovimentacoes(valorPago)
                .saldoInicial(saldoInicial)
                .saldoAtual(saldoAtual)
                .build();
    }
}