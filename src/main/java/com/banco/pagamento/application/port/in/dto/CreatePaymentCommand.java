package com.banco.pagamento.application.port.in.dto;

import com.banco.pagamento.domain.model.PaymentMethod;

import java.math.BigDecimal;

public record CreatePaymentCommand(
        BigDecimal amount,
        PaymentMethod method,
        String description
) {
}
