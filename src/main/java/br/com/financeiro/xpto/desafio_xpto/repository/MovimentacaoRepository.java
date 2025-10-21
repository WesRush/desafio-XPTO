package br.com.financeiro.xpto.desafio_xpto.repository;

import br.com.financeiro.xpto.desafio_xpto.entity.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
}
