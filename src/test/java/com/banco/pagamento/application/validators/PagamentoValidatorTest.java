package com.banco.pagamento.application.validators;

import com.banco.pagamento.application.port.in.dto.CreatePaymentCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static com.banco.pagamento.domain.model.PaymentMethod.PIX;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PagamentoValidatorTest {

    @InjectMocks
    private PagamentoValidator validator;

    @Test
    @DisplayName("Deve validar o comando de pagamento")
    void comandoPagamentoInvalido() {
        CreatePaymentCommand command = new CreatePaymentCommand(
                null,
                null,
                null
        );

        assertThrows(IllegalArgumentException.class, () -> validator.validate(command));
    }

    @Test
    @DisplayName("Deve validar o valor do pagamento")
    void valorPagamentoInvalido() {
        CreatePaymentCommand command = new CreatePaymentCommand(
                BigDecimal.ZERO,
                PIX,
                "Teste"
        );

        assertThrows(IllegalArgumentException.class, () -> validator.validate(command));
    }

    @Test
    @DisplayName("Deve validar a forma de pagamento")
    void formaPagamentoInvalido() {
        CreatePaymentCommand command = new CreatePaymentCommand(
                new BigDecimal("100.00"),
                null,
                "Teste"
        );

        assertThrows(IllegalArgumentException.class, () -> validator.validate(command));
    }
}