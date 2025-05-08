package com.municipio.simple.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TipoServicoRequest {

    @NotBlank(message = "O código do tipo de serviço não pode estar em branco.")
    @Size(max = 50, message = "O código do tipo de serviço deve ter no máximo 50 caracteres.")
    private String codigo;

    @NotBlank(message = "O nome do tipo de serviço não pode estar em branco.")
    @Size(max = 255, message = "O nome do tipo de serviço deve ter no máximo 255 caracteres.")
    private String nome;

    @Size(max = 1000, message = "A descrição do tipo de serviço deve ter no máximo 1000 caracteres.")
    private String descricao;

    @NotNull(message = "O ID da categoria não pode ser nulo.")
    private Integer categoriaId;

    private Integer prazoEstimado; // em dias

    private BigDecimal valorBase;

    @NotNull(message = "O campo 'requerVistoria' não pode ser nulo.")
    private Boolean requerVistoria = false;

    @NotNull(message = "O campo 'requerAnaliseTecnica' não pode ser nulo.")
    private Boolean requerAnaliseTecnica = false;

    @NotNull(message = "O campo 'requerAprovacao' não pode ser nulo.")
    private Boolean requerAprovacao = true;

    @NotNull(message = "O campo 'disponivelPortal' não pode ser nulo.")
    private Boolean disponivelPortal = false;

    @NotNull(message = "O campo 'ativo' não pode ser nulo.")
    private Boolean ativo = true;
}