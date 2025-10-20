package br.com.financeiro.xpto.desafio_xpto.entity;

import com.sun.xml.bind.v2.TODO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Entity
@Table(name="cliente")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_cliente")
@Data
@NoArgsConstructor
public abstract class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    @NotEmpty(message = "Nome obrigatório")
    private String nome;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "data_cadastro, updatable = false")
    private LocalDate dataCadastro;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Endereco endereco;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Conta> contas = new ArrayList<>();

    @PrePersist
    public void prePersist(){
        setDataCadastro(LocalDate.now());
    }
}
