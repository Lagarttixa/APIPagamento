package com.banco.pagamento.application.validators;

import com.banco.pagamento.application.port.in.dto.CreatePaymentCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static com.banco.pagamento.domain.model.PaymentMethod.PIX;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PagamentoValidatorTest {

    @InjectMocks
    private PagamentoValidator validator;

    @Nested
    @DisplayName("Validação de pagamento")
    class validate {

        @Test
        @DisplayName("Deve lançar exceção quando comando de pagamento for nulo")
        void deveLancarExcecaoQuandoComandoPagamentoForNulo() {
            CreatePaymentCommand command = null;

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validate(command));

            assertEquals("Comando de pagamento deve ser informado.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando valor do pagamento for inválido")
        void deveLancarExcecaoQuandoValorPagamentoForInvalido() {
            CreatePaymentCommand command = new CreatePaymentCommand(
                    BigDecimal.ZERO,
                    PIX,
                    "Teste"
            );

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validate(command));

            assertEquals("Valor deve ser maior que zero.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando forma de pagamento for inválida")
        void deveLancarExcecaoQuandoFormaPagamentoForInvalida() {
            CreatePaymentCommand command = new CreatePaymentCommand(
                    new BigDecimal("100.00"),
                    null,
                    "Teste"
            );

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validate(command));

            assertEquals("Forma de pagamento deve ser informada.", ex.getMessage());
        }
    }
}
