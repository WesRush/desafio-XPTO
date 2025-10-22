package br.com.financeiro.xpto.desafio_xpto.service;

import br.com.financeiro.xpto.desafio_xpto.entity.Endereco;
import br.com.financeiro.xpto.desafio_xpto.exception.ResourceNotFoundException;
import br.com.financeiro.xpto.desafio_xpto.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;

    @Autowired
    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    @Transactional(readOnly = true) // Bom para consultas
    public Endereco buscarEnderecoPorId(Long id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado com ID: " + id));
    }

}
