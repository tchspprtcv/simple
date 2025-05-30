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
public class EtapaProcessoRequest {

    @NotBlank(message = "Código não pode ser vazio")
    @Size(max = 50, message = "Código deve ter no máximo 50 caracteres")
    private String codigo;

    @NotBlank(message = "Nome não pode ser vazio")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    private String descricao;

    @NotNull(message = "Ordem não pode ser nula")
    @Min(value = 1, message = "Ordem deve ser no mínimo 1")
    private Integer ordem;

    @Min(value = 0, message = "Tempo estimado não pode ser negativo")
    private Integer tempoEstimado; // em horas

    @NotNull(message = "Requer Documento não pode ser nulo")
    private Boolean requerDocumento = false;

    @NotNull(message = "Requer Pagamento não pode ser nulo")
    private Boolean requerPagamento = false;

    @NotNull(message = "Requer Aprovação não pode ser nulo")
    private Boolean requerAprovacao = false;

    @NotNull(message = "ID do Tipo de Serviço não pode ser nulo")
    private Integer tipoServicoId;

    private Integer perfilResponsavelId;

    private Integer etapaAnteriorId;
}
