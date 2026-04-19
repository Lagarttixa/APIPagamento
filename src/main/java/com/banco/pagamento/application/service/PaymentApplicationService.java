package com.banco.pagamento.application.service;

import com.banco.pagamento.application.port.in.PaymentQueryUseCase;
import com.banco.pagamento.application.port.in.ProcessPaymentUseCase;
import com.banco.pagamento.application.port.in.dto.CreatePaymentCommand;
import com.banco.pagamento.application.port.in.dto.PaymentResponse;
import com.banco.pagamento.domain.model.Payment;
import com.banco.pagamento.domain.model.PaymentMethod;
import com.banco.pagamento.domain.port.out.PaymentRepository;
import com.banco.pagamento.domain.strategy.PaymentStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PaymentApplicationService implements ProcessPaymentUseCase, PaymentQueryUseCase {
    private final PaymentRepository paymentRepository;
    private final Map<PaymentMethod, PaymentStrategy> strategiesByMethod;

    public PaymentApplicationService(PaymentRepository paymentRepository,
                                     List<PaymentStrategy> strategiesByMethod
    ) {
        this.paymentRepository = paymentRepository;
        this.strategiesByMethod = strategiesByMethod.stream()
                .collect(Collectors.toMap(PaymentStrategy::getPaymentMethod, Function.identity()));
    }

    @Override
    public PaymentResponse process(CreatePaymentCommand command) {
        validate(command);

        Payment payment = Payment.newPayment(command.amount(), command.method(), command.description());
        Payment processed = processPaymentByMethod(payment);
        Payment saved = paymentRepository.save(processed);
        return PaymentResponse.from(saved);
    }

    @Override
    public PaymentResponse findById(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .map(PaymentResponse::from)
                .orElseThrow(() -> new NoSuchElementException("Pagamento nao encontrado: " + paymentId));
    }

    @Override
    public List<PaymentResponse> findAll() {
        return paymentRepository.findAll().stream()
                .map(PaymentResponse::from)
                .toList();
    }

    private void validate(CreatePaymentCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Comando de pagamento deve ser informado.");
        }
        if (command.amount() == null || command.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero.");
        }
        if (command.method() == null) {
            throw new IllegalArgumentException("Forma de pagamento deve ser informada.");
        }
    }

    private Payment processPaymentByMethod(Payment payment) {
        PaymentStrategy strategy = strategiesByMethod.get(payment.method());
        if (strategy == null) {
            throw new IllegalArgumentException("Forma de pagamento invalida: " + payment.method());
        }
        return strategy.execute(payment);
    }
}
