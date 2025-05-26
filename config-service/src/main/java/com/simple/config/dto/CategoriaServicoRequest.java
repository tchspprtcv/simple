package com.simple.config.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaServicoRequest {
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255, message = "Nome não pode exceder 255 caracteres")
    private String nome;

    @Size(max = 1000, message = "Descrição não pode exceder 1000 caracteres")
    private String descricao;

    @Size(max = 100, message = "Ícone não pode exceder 100 caracteres")
    private String icone;

    @Size(max = 50, message = "Cor não pode exceder 50 caracteres")
    private String cor;

    @NotNull(message = "Ordem é obrigatória")
    private Integer ordem = 0;

    @NotNull(message = "Ativo é obrigatório")
    private Boolean ativo = true;
}
