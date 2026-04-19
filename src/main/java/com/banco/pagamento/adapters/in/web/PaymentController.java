package com.banco.pagamento.adapters.in.web;

import com.banco.pagamento.adapters.in.web.dto.CreatePaymentRequest;
import com.banco.pagamento.application.port.in.PaymentQueryUseCase;
import com.banco.pagamento.application.port.in.ProcessPaymentUseCase;
import com.banco.pagamento.application.port.in.dto.CreatePaymentCommand;
import com.banco.pagamento.application.port.in.dto.PaymentResponse;
import com.banco.pagamento.domain.model.PaymentMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pagamentos")
public class PaymentController {
    private final ProcessPaymentUseCase processPaymentUseCase;
    private final PaymentQueryUseCase paymentQueryUseCase;

    public PaymentController(ProcessPaymentUseCase processPaymentUseCase, PaymentQueryUseCase paymentQueryUseCase) {
        this.processPaymentUseCase = processPaymentUseCase;
        this.paymentQueryUseCase = paymentQueryUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse create(@RequestBody CreatePaymentRequest request) {
        CreatePaymentCommand command = new CreatePaymentCommand(
                request.amount(),
                PaymentMethod.from(request.method()),
                request.description()
        );
        return processPaymentUseCase.process(command);
    }

    @GetMapping("/{id}")
    public PaymentResponse findById(@PathVariable UUID id) {
        return paymentQueryUseCase.findById(id);
    }

    @GetMapping
    public List<PaymentResponse> findAll() {
        return paymentQueryUseCase.findAll();
    }
}
