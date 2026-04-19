package com.banco.pagamento.application.port.in;

import com.banco.pagamento.application.port.in.dto.CreatePaymentCommand;
import com.banco.pagamento.application.port.in.dto.PaymentResponse;

public interface ProcessPaymentUseCase {
    PaymentResponse process(CreatePaymentCommand command);
}
