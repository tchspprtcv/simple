package com.municipio.simple.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoritoResponse {
    private Integer id;
    private Integer tipoServicoId;
    private String tipoServicoNome;
    private String tipoServicoCodigo;
    private Integer ordem;
    private LocalDateTime criadoEm;
}