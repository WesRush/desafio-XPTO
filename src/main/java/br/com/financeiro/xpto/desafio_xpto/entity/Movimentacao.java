package br.com.financeiro.xpto.desafio_xpto.entity;

import br.com.financeiro.xpto.desafio_xpto.domain.enums.TipoMovimentacao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_hora_movimentacao", updatable = false)
    private LocalDateTime dataHoraMovimentacao;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "valor", nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;


    @Column(name = "tipo_movimentacao", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipoMovimentacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id")
    @JsonIgnore
    private Conta conta;

    @PrePersist
    public void prePersist() {
        setDataHoraMovimentacao(LocalDateTime.now());
    }

}
