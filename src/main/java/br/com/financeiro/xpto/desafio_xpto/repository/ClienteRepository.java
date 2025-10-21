package br.com.financeiro.xpto.desafio_xpto.repository;

import br.com.financeiro.xpto.desafio_xpto.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
