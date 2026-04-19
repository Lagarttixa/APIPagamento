package com.banco.pagamento.domain.port.out;

import com.banco.pagamento.domain.model.Payment;

public interface PaymentGatewayPort {
    String processPix(Payment payment);

    String processCreditCard(Payment payment);

    String processBoleto(Payment payment);
}
