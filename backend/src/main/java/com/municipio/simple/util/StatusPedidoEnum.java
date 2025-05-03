package com.municipio.simple.util;

/**
 * Enumeração dos status possíveis para um pedido
 */
public enum StatusPedidoEnum {
    
    NOVO("NOVO", "Novo", "Pedido recém-criado"),
    EM_ANALISE("EM_ANALISE", "Em Análise", "Pedido em análise pelos técnicos"),
    AGUARDANDO_DOCUMENTOS("AGUARDANDO_DOC", "Aguardando Documentos", "Aguardando documentos adicionais"),
    AGUARDANDO_PAGAMENTO("AGUARDANDO_PAG", "Aguardando Pagamento", "Aguardando pagamento de taxas"),
    AGENDADO("AGENDADO", "Vistoria Agendada", "Vistoria agendada"),
    EM_VISTORIA("EM_VISTORIA", "Em Vistoria", "Vistoria em andamento"),
    APROVADO("APROVADO", "Aprovado", "Pedido aprovado"),
    REPROVADO("REPROVADO", "Reprovado", "Pedido reprovado"),
    CONCLUIDO("CONCLUIDO", "Concluído", "Processo concluído"),
    CANCELADO("CANCELADO", "Cancelado", "Processo cancelado");
    
    private final String codigo;
    private final String nome;
    private final String descricao;
    
    StatusPedidoEnum(String codigo, String nome, String descricao) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public static StatusPedidoEnum getByCodigo(String codigo) {
        for (StatusPedidoEnum status : values()) {
            if (status.getCodigo().equals(codigo)) {
                return status;
            }
        }
        return null;
    }
}
