package com.banco.pagamento.application.validators;

import com.banco.pagamento.application.port.in.dto.CreatePaymentCommand;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PagamentoValidator {

    public void validate(CreatePaymentCommand command) {
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
}
