package com.simple.citizen.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;
    
    @NotBlank(message = "Tipo de documento é obrigatório")
    private String tipoDocumento; // e.g., "CNI", "BI"
    
    @NotBlank(message = "Número de documento é obrigatório")
    @Size(min = 5, max = 20, message = "Número de documento deve ter entre 5 e 20 caracteres")
    private String numeroDocumento;
    
    @Email(message = "Email inválido")
    @Size(max = 100, message = "Email não pode exceder 100 caracteres")
    private String email; // Optional, but if provided, must be valid email
    
    @Size(max = 20, message = "Telefone não pode exceder 20 caracteres")
    private String telefone; // Optional
    
    @Size(max = 255, message = "Endereço não pode exceder 255 caracteres")
    private String endereco; // Optional
}
