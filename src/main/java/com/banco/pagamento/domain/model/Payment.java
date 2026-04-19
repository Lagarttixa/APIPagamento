package com.banco.pagamento.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record Payment(
        UUID id,
        BigDecimal amount,
        PaymentMethod method,
        String description,
        PaymentStatus status,
        String gatewayReference,
        Instant createdAt,
        Instant updatedAt
) {
    public static Payment newPayment(BigDecimal amount, PaymentMethod method, String description) {
        Instant now = Instant.now();
        return new Payment(
                UUID.randomUUID(),
                amount,
                method,
                description,
                PaymentStatus.PENDENTE,
                null,
                now,
                now
        );
    }

    public Payment updateStatus(PaymentStatus status, String gatewayReference) {
        return new Payment(
                this.id,
                this.amount,
                this.method,
                this.description,
                status,
                gatewayReference,
                this.createdAt,
                Instant.now()
        );
    }
}
