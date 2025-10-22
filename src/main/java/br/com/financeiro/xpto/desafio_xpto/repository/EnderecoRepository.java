package br.com.financeiro.xpto.desafio_xpto.repository;

import br.com.financeiro.xpto.desafio_xpto.entity.Endereco;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco,Long> {
}
