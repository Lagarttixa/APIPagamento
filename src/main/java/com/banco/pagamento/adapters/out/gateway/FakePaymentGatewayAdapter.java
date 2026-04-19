package com.banco.pagamento.adapters.out.gateway;

import com.banco.pagamento.domain.model.Payment;
import com.banco.pagamento.domain.port.out.PaymentGatewayPort;
import org.springframework.stereotype.Component;

@Component
public class FakePaymentGatewayAdapter implements PaymentGatewayPort {
    @Override
    public String processPix(Payment payment) {
        return "PIX-" + payment.id();
    }

    @Override
    public String processCreditCard(Payment payment) {
        return "CC-" + payment.id();
    }

    @Override
    public String processBoleto(Payment payment) {
        return "BOLETO-" + payment.id();
    }
}
