package com.municipio.simple.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utilitário para geração de códigos de acompanhamento para pedidos
 */
public class CodigoAcompanhamentoUtil {

    private static final String PREFIXO = "SMP";
    private static final DateTimeFormatter FORMATO_ANO = DateTimeFormatter.ofPattern("yy");

    /**
     * Gera um código de acompanhamento no formato SMP-AANNNNNN
     * onde AA é o ano atual e NNNNNN é um número sequencial
     *
     * @return Código de acompanhamento gerado
     */
    public static String gerarCodigoAcompanhamento() {
        String ano = LocalDate.now().format(FORMATO_ANO);
        int sequencial = ThreadLocalRandom.current().nextInt(1, 999999);
        return String.format("%s-%s%06d", PREFIXO, ano, sequencial);
    }
}
