package com.simple.order.util;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CodigoAcompanhamentoUtil {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 8; // You can adjust the length
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String gerarCodigo() {
        return IntStream.range(0, CODE_LENGTH)
                .map(i -> RANDOM.nextInt(CHARACTERS.length()))
                .mapToObj(CHARACTERS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
