package com.simple.config.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilResponseDTO {
    private Integer id;
    private String nome;
    private String descricao;
}
