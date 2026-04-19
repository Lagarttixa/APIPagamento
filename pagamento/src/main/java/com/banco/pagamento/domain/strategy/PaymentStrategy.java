package com.banco.pagamento.domain.strategy;

import com.banco.pagamento.domain.model.Payment;
import com.banco.pagamento.domain.model.PaymentMethod;

public interface PaymentStrategy {
    PaymentMethod getPaymentMethod();

    Payment execute(Payment payment);
}
