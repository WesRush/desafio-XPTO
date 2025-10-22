package br.com.financeiro.xpto.desafio_xpto.service;

import br.com.financeiro.xpto.desafio_xpto.dto.ContaDTO;
import br.com.financeiro.xpto.desafio_xpto.entity.Cliente;
import br.com.financeiro.xpto.desafio_xpto.entity.Conta;
import br.com.financeiro.xpto.desafio_xpto.exception.RegraNegocioException;
import br.com.financeiro.xpto.desafio_xpto.exception.ResourceNotFoundException;
import br.com.financeiro.xpto.desafio_xpto.repository.ClienteRepository;
import br.com.financeiro.xpto.desafio_xpto.repository.ContaRepository;
import br.com.financeiro.xpto.desafio_xpto.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final ClienteRepository clienteRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    @Autowired
    public ContaService(ContaRepository contaRepository, ClienteRepository clienteRepository, MovimentacaoRepository movimentacaoRepository) {
        this.contaRepository = contaRepository;
        this.clienteRepository = clienteRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }
    @Transactional
    public Conta criarConta(Long clienteId, ContaDTO contaDTO) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + clienteId));

        Conta novaConta = new Conta();
        novaConta.setNomeBanco(contaDTO.getNomeBanco());
        novaConta.setAgencia(contaDTO.getAgencia());
        novaConta.setNumeroConta(contaDTO.getNumeroConta());
        novaConta.setAtivo(true);
        novaConta.setCliente(cliente);

        return contaRepository.save(novaConta);
    }
    @Transactional(readOnly = true)
    public Conta buscarContaAtivaPorId(Long id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada com ID: " + id));
        if (!conta.isAtivo()) {
            throw new ResourceNotFoundException("Conta encontrada, mas está inativa (ID: " + id + ")");
        }
        return conta;
    }
    @Transactional(readOnly = true)
    public List<Conta> listarContasAtivasPorCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new ResourceNotFoundException("Cliente não encontrado com ID: " + clienteId);
        }
        return contaRepository.findByClienteId(clienteId)
                .stream()
                .filter(Conta::isAtivo)
                .collect(Collectors.toList());
    }
    @Transactional
    public Conta atualizarConta(Long id, ContaDTO contaDTO) {
        Conta contaExistente = buscarContaAtivaPorId(id);
        long countMovimentacoes = movimentacaoRepository.countByContaId(id);
        if (countMovimentacoes > 0) {
            throw new RegraNegocioException("Não é possível alterar a conta (ID: " + id + ") pois ela possui movimentações associadas.");
        }
        contaExistente.setNomeBanco(contaDTO.getNomeBanco());
        contaExistente.setAgencia(contaDTO.getAgencia());
        contaExistente.setNumeroConta(contaDTO.getNumeroConta());

        return contaRepository.save(contaExistente);
    }
    @Transactional
    public void deletarContaLogicamente(Long id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada com ID: " + id));

        if (!conta.isAtivo()) {
            throw new RegraNegocioException("Conta (ID: " + id + ") já está inativa.");
        }

        conta.setAtivo(false);
        contaRepository.save(conta);
    }
}
