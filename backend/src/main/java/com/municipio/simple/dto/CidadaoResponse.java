package com.municipio.simple.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CidadaoResponse {
    private UUID id;
    private String nome;
    private String tipoDocumento;
    private String numeroDocumento;
    private String email;
    private String telefone;
    private String endereco;
    private LocalDateTime criadoEm;
}
