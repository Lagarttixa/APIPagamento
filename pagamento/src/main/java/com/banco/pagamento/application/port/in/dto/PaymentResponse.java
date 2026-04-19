package com.banco.pagamento.application.port.in.dto;

import com.banco.pagamento.domain.model.Payment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        BigDecimal amount,
        String method,
        String description,
        String status,
        String gatewayReference,
        Instant createdAt,
        Instant updatedAt
) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.id(),
                payment.amount(),
                payment.method().name(),
                payment.description(),
                payment.status().name(),
                payment.gatewayReference(),
                payment.createdAt(),
                payment.updatedAt()
        );
    }
}
