package br.com.financeiro.xpto.desafio_xpto.repository;

import br.com.financeiro.xpto.desafio_xpto.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaRepository extends JpaRepository<Conta,Long> {
}
