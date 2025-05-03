package com.municipio.simple.util;

/**
 * Enumeração dos tipos de processos suportados pelo sistema
 */
public enum TipoProcessoEnum {
    
    LEGALIZACAO("Legalização"),
    COMPRA_LOTE("Compra de Lote"),
    PEDIDO_EVENTO("Pedido de Eventos"),
    PAGAMENTO_PRESTACAO("Pedido de Pagamento em Prestação"),
    MUDANCA_NOME("Mudança de Nome"),
    ATUALIZACAO_PLANTA("Atualização de Planta"),
    EMISSAO_PLANTA("Emissão de Planta"),
    LICENCIAMENTO_COMERCIAL_PRIMEIRA_VEZ("Licenciamento Comercial - 1ª Vez"),
    LICENCIAMENTO_COMERCIAL_RENOVACAO("Licenciamento Comercial - Renovação"),
    APROVACAO_PROJETOS("Aprovação de Projetos"),
    LICENCA_CONSTRUCAO_PRIMEIRA_VEZ("Licença de Construção - 1ª Vez"),
    LICENCA_CONSTRUCAO_RENOVACAO("Licença de Construção - Renovação");
    
    private final String descricao;
    
    TipoProcessoEnum(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
