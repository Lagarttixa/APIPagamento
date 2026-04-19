package com.banco.pagamento.application.port.in;

import com.banco.pagamento.application.port.in.dto.PaymentResponse;

import java.util.List;
import java.util.UUID;

public interface PaymentQueryUseCase {
    PaymentResponse findById(UUID paymentId);

    List<PaymentResponse> findAll();
}
