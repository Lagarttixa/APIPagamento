package com.banco.pagamento.adapters.out.strategy;

import com.banco.pagamento.domain.model.Payment;
import com.banco.pagamento.domain.model.PaymentMethod;
import com.banco.pagamento.domain.model.PaymentStatus;
import com.banco.pagamento.domain.port.out.PaymentGatewayPort;
import com.banco.pagamento.domain.strategy.PaymentStrategy;
import org.springframework.stereotype.Component;

@Component
public class PixPaymentStrategy implements PaymentStrategy {

    private final PaymentGatewayPort gatewayPort;

    public PixPaymentStrategy(PaymentGatewayPort gatewayPort) {
        this.gatewayPort = gatewayPort;
    }

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.PIX;
    }

    @Override
    public Payment execute(Payment payment) {
        String gatewayReference = gatewayPort.processPix(payment);
        return payment.updateStatus(PaymentStatus.APROVADO, gatewayReference);
    }
}
