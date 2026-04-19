package com.banco.pagamento.domain.model;

import java.util.Locale;

public enum PaymentMethod {
    PIX,
    CARTAO_CREDITO,
    BOLETO;

    public static PaymentMethod from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Forma de pagamento deve ser informada.");
        }

        String normalized = value
                .trim()
                .toUpperCase(Locale.ROOT)
                .replace('-', '_')
                .replace(' ', '_');

        return switch (normalized) {
            case "PIX" -> PIX;
            case "CARTAO", "CARTAO_CREDITO", "CREDITO", "CREDIT_CARD" -> CARTAO_CREDITO;
            case "BOLETO" -> BOLETO;
            default -> throw new IllegalArgumentException("Forma de pagamento invalida: " + value);
        };
    }
}
