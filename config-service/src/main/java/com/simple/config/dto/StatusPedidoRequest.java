package com.simple.config.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusPedidoRequest {

    @NotBlank(message = "Código não pode ser vazio")
    @Size(max = 50, message = "Código deve ter no máximo 50 caracteres")
    private String codigo;

    @NotBlank(message = "Nome não pode ser vazio")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    private String descricao;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Cor deve estar em formato hexadecimal (ex: #RRGGBB)")
    private String cor;

    @Size(max = 50, message = "Ícone deve ter no máximo 50 caracteres")
    private String icone;

    private Integer ordem;

    @NotNull(message = "Visibilidade no portal não pode ser nula")
    private Boolean visivelPortal = true;
}
