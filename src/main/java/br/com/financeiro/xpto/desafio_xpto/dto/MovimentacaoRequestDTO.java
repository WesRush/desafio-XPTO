package br.com.financeiro.xpto.desafio_xpto.dto;
import br.com.financeiro.xpto.desafio_xpto.domain.enums.TipoMovimentacao;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class MovimentacaoRequestDTO {

    @NotNull(message = "A descrição é obrigatória.")
    private String descricao;

    @NotNull(message = "O valor é obrigatório.")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
    private BigDecimal valor;

    @NotNull(message = "O tipo da movimentação (CREDITO ou DEBITO) é obrigatório.")
    private TipoMovimentacao tipoMovimentacao;

}
