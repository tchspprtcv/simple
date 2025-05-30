package com.simple.config.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoDocumentoRequest {

    @NotBlank(message = "Código não pode ser vazio")
    @Size(max = 50, message = "Código deve ter no máximo 50 caracteres")
    private String codigo;

    @NotBlank(message = "Nome não pode ser vazio")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    private String descricao;

    @Size(max = 255, message = "Formato permitido deve ter no máximo 255 caracteres")
    private String formatoPermitido; // e.g., "pdf,jpg,png"

    @Min(value = 1, message = "Tamanho máximo deve ser no mínimo 1 KB")
    private Integer tamanhoMaximo; // em KB

    @NotNull(message = "Obrigatoriedade não pode ser nula")
    private Boolean obrigatorio = false;

    @NotNull(message = "Status de ativo não pode ser nulo")
    private Boolean ativo = true;
}
