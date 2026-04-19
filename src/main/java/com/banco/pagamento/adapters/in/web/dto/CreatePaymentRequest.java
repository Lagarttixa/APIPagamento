package com.banco.pagamento.adapters.in.web.dto;

import java.math.BigDecimal;

public record CreatePaymentRequest(
        BigDecimal amount,
        String method,
        String description
) {
}
