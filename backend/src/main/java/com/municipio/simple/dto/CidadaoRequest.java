package com.municipio.simple.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CidadaoRequest {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @NotBlank(message = "Tipo de documento é obrigatório")
    private String tipoDocumento;
    
    @NotBlank(message = "Número de documento é obrigatório")
    private String numeroDocumento;
    
    @Email(message = "Email inválido")
    private String email;
    
    private String telefone;
    private String endereco;
}
