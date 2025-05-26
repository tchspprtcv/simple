package com.simple.config.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoServicoRequest {

    @NotBlank(message = "Código é obrigatório")
    @Size(max = 50, message = "Código deve ter no máximo 50 caracteres")
    private String codigo;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    private String nome;

    @Size(max = 2000, message = "Descrição deve ter no máximo 2000 caracteres")
    private String descricao;

    @NotNull(message = "ID da categoria é obrigatório")
    private Integer categoriaId;

    @PositiveOrZero(message = "Prazo estimado deve ser um número positivo ou zero")
    private Integer prazoEstimadoDias;

    @PositiveOrZero(message = "Valor base deve ser um número positivo ou zero")
    private BigDecimal valorBase;

    @NotNull(message = "'Requer vistoria' é obrigatório")
    private Boolean requerVistoria = false;

    @NotNull(message = "'Requer análise técnica' é obrigatório")
    private Boolean requerAnaliseTecnica = false;

    @NotNull(message = "'Requer aprovação' é obrigatório")
    private Boolean requerAprovacao = true;

    @NotNull(message = "'Disponível no portal' é obrigatório")
    private Boolean disponivelPortal = false;

    @NotNull(message = "'Ativo' é obrigatório")
    private Boolean ativo = true;
}
