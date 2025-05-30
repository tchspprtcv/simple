package com.simple.config.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoDocumentoResponse {
    private Integer id;
    private String codigo;
    private String nome;
    private String descricao;
    private String formatoPermitido;
    private Integer tamanhoMaximo; // em KB
    private boolean obrigatorio;
    private boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
