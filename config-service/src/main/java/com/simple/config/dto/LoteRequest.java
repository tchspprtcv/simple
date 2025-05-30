package com.simple.config.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoteRequest {

    @NotBlank(message = "Código não pode ser vazio")
    @Size(max = 50, message = "Código deve ter no máximo 50 caracteres")
    private String codigo;

    @Size(max = 50, message = "Quadra deve ter no máximo 50 caracteres")
    private String quadra;

    @Size(max = 50, message = "Número deve ter no máximo 50 caracteres")
    private String numero;

    @NotNull(message = "Área não pode ser nula")
    @DecimalMin(value = "0.01", message = "Área deve ser maior que zero")
    private BigDecimal area; // em m²

    @Size(max = 255, message = "Endereço deve ter no máximo 255 caracteres")
    private String endereco;

    @Size(max = 100, message = "Bairro deve ter no máximo 100 caracteres")
    private String bairro;

    @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
    private String cidade;

    @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres (UF)")
    private String estado;

    @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "CEP deve estar no formato XXXXX-XXX ou XXXXXXXX")
    private String cep;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @NotBlank(message = "Situação não pode ser vazia")
    @Size(max = 50, message = "Situação deve ter no máximo 50 caracteres")
    private String situacao; // e.g., DISPONIVEL, VENDIDO, RESERVADO

    @NotNull(message = "Valor base não pode ser nulo")
    @DecimalMin(value = "0.00", message = "Valor base não pode ser negativo")
    private BigDecimal valorBase;

    private String observacoes;
}
