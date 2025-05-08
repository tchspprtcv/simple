package com.municipio.simple.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoServicoResponse {
    private Integer id;
    private Integer categoriaId; // Apenas o ID da categoria para simplificar
    private String categoriaNome; // Nome da categoria para exibição
    private String codigo;
    private String nome;
    private String descricao;
    private Integer prazoEstimado;
    private BigDecimal valorBase;
    private boolean requerVistoria;
    private boolean requerAnaliseTecnica;
    private boolean requerAprovacao;
    private boolean disponivelPortal;
    private boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    // Construtores, getters e setters gerados pelo Lombok
    // Mapeamento de/para entidade pode ser adicionado aqui ou no serviço/controller
}