package br.com.financeiro.xpto.desafio_xpto.service;

import br.com.financeiro.xpto.desafio_xpto.domain.enums.TipoMovimentacao;
import br.com.financeiro.xpto.desafio_xpto.dto.ClienteRequestDTO;
import br.com.financeiro.xpto.desafio_xpto.dto.ContaInicialDTO;
import br.com.financeiro.xpto.desafio_xpto.dto.EnderecoDTO;
import br.com.financeiro.xpto.desafio_xpto.entity.Cliente;
import br.com.financeiro.xpto.desafio_xpto.entity.Conta;
import br.com.financeiro.xpto.desafio_xpto.entity.Movimentacao;
import br.com.financeiro.xpto.desafio_xpto.entity.PessoaFisica;
import br.com.financeiro.xpto.desafio_xpto.repository.ClienteRepository;
import br.com.financeiro.xpto.desafio_xpto.repository.MovimentacaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private MovimentacaoRepository movimentacaoRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks ClienteService clienteService;

    @Test
    void deveCriarNovoClientePessoaFisicaComSucesso() {

        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setRua("Rua Teste");
        ContaInicialDTO contaDTO = new ContaInicialDTO();
        contaDTO.setNomeBanco("Banco Teste");
        contaDTO.setAgencia("1234");
        contaDTO.setNumeroConta("56789-0");
        contaDTO.setSaldoInicial(new BigDecimal("100.00"));

        ClienteRequestDTO requestDTO = new ClienteRequestDTO();
        requestDTO.setNome("Cliente Teste PF");
        requestDTO.setTipoCliente("PF");
        requestDTO.setCpfCnpj("12345678901");
        requestDTO.setTelefone("999998888");
        requestDTO.setEndereco(enderecoDTO);
        requestDTO.setContaInicial(contaDTO);

        PessoaFisica clienteRetornadoComId = new PessoaFisica();
        clienteRetornadoComId.setId(1L); // Define o ID
        clienteRetornadoComId.setNome(requestDTO.getNome());
        clienteRetornadoComId.setCpf(requestDTO.getCpfCnpj());

        when(clienteRepository.save(any(Cliente.class)))
                .thenReturn(clienteRetornadoComId);

        Cliente clienteCriado = clienteService.criarNovoCliente(requestDTO);


        ArgumentCaptor<Cliente> clienteCaptor = ArgumentCaptor.forClass(Cliente.class);
        verify(clienteRepository, times(1)).save(clienteCaptor.capture());
        Cliente clientePassadoParaSave = clienteCaptor.getValue();


        assertNotNull(clienteCriado, "O cliente retornado pelo serviço não pode ser nulo");
        assertNotNull(clienteCriado.getId(), "O ID do cliente retornado pelo serviço não pode ser nulo");
        assertEquals(1L, clienteCriado.getId(), "O ID do cliente retornado deve ser o simulado (1L)");


        assertNotNull(clientePassadoParaSave, "O objeto passado para o repository.save não pode ser nulo");
        assertTrue(clientePassadoParaSave instanceof PessoaFisica, "O cliente passado para save deve ser PessoaFisica");
        assertEquals("Cliente Teste PF", clientePassadoParaSave.getNome());
        assertEquals("12345678901", ((PessoaFisica) clientePassadoParaSave).getCpf());
        assertEquals("999998888", clientePassadoParaSave.getTelefone());


        assertNotNull(clientePassadoParaSave.getEndereco(), "O endereço no objeto passado para save não pode ser nulo");
        assertEquals("Rua Teste", clientePassadoParaSave.getEndereco().getRua());


        assertNotNull(clientePassadoParaSave.getContas(), "A lista de contas não pode ser nula");
        assertEquals(1, clientePassadoParaSave.getContas().size(), "Deve haver 1 conta na lista");
        Conta contaSalva = clientePassadoParaSave.getContas().get(0);
        assertEquals("Banco Teste", contaSalva.getNomeBanco());
        assertNotNull(contaSalva.getMovimentacoes(), "A lista de movimentações da conta não pode ser nula");
        assertEquals(1, contaSalva.getMovimentacoes().size(), "Deve haver 1 movimentação inicial na conta");
        Movimentacao movInicialSalva = contaSalva.getMovimentacoes().get(0);
        assertEquals("Depósito Inicial", movInicialSalva.getDescricao());
        assertEquals(0, new BigDecimal("100.00").compareTo(movInicialSalva.getValor()), "O valor da movimentação inicial deve ser 100.00");
        assertEquals(TipoMovimentacao.CREDITO, movInicialSalva.getTipoMovimentacao());
    }
}
