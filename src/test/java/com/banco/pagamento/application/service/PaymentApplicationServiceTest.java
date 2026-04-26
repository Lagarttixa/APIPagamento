package com.banco.pagamento.application.service;

import com.banco.pagamento.application.port.in.dto.CreatePaymentCommand;
import com.banco.pagamento.application.port.in.dto.PaymentResponse;
import com.banco.pagamento.application.validators.PagamentoValidator;
import com.banco.pagamento.domain.model.Payment;
import com.banco.pagamento.domain.model.PaymentMethod;
import com.banco.pagamento.domain.model.PaymentStatus;
import com.banco.pagamento.domain.port.out.PaymentRepository;
import com.banco.pagamento.domain.strategy.PaymentStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentApplicationServiceTest {

    PaymentApplicationService paymentApplicationService;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    private PaymentStrategy paymentStrategy;

    @Mock
    PagamentoValidator pagamentoValidator;

    @BeforeEach
    void setUp() {
        when(paymentStrategy.getPaymentMethod()).thenReturn(PaymentMethod.PIX);
        paymentApplicationService = new PaymentApplicationService(
                paymentRepository,
                List.of(paymentStrategy),
                pagamentoValidator
        );
    }

    @Nested
    @DisplayName("Processamento de pagamento")
    class process {

        @Test
        @DisplayName("Deve processar um pagamento com sucesso")
        void deveProcessarPagamentoComSucesso() {
            CreatePaymentCommand command = new CreatePaymentCommand(new BigDecimal("100.00"), PaymentMethod.PIX, "teste");

            Payment payment = Payment.newPayment(command.amount(), command.method(), command.description());
            Payment processedPayment = payment.updateStatus(PaymentStatus.APROVADO, "REF123");
            Payment savedPayment = processedPayment;

            when(paymentStrategy.execute(any(Payment.class))).thenReturn(processedPayment);
            when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

            PaymentResponse response = paymentApplicationService.process(command);

            verify(pagamentoValidator, times(1)).validate(command);
            verify(paymentRepository, times(1)).save(processedPayment);
            assertEquals(savedPayment.id(), response.id());
            assertEquals(savedPayment.amount(), response.amount());
            assertEquals(savedPayment.method().name(), response.method());
            assertEquals(savedPayment.description(), response.description());
            assertEquals(savedPayment.status().name(), response.status());
        }

        @Test
        @DisplayName("Deve falhar ao processar pagamento com forma inválida")
        void deveFalharAoProcessarPagamentoComFormaInvalida() {
            CreatePaymentCommand command =
                    new CreatePaymentCommand(new BigDecimal("100.00"), PaymentMethod.BOLETO, "teste");

            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> paymentApplicationService.process(command)
            );

            assertEquals("Forma de pagamento invalida: BOLETO", ex.getMessage());
            verify(paymentStrategy, never()).execute(any());
            verify(paymentRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Busca de pagamento por ID")
    class findById {

        @Test
        @DisplayName("Deve encontrar pagamento por ID")
        void deveEncontrarPagamentoPorId() {
            UUID paymentId = UUID.randomUUID();
            Payment payment = Payment.newPayment(new BigDecimal("100.00"), PaymentMethod.PIX, "teste");

            when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
            PaymentResponse response = paymentApplicationService.findById(paymentId);

            assertEquals(payment.id(), response.id());
            assertEquals(payment.amount(), response.amount());
            assertEquals(payment.method().name(), response.method());
            assertEquals(payment.description(), response.description());
            assertEquals(payment.status().name(), response.status());

            verify(paymentRepository, times(1)).findById(paymentId);
        }

        @Test
        @DisplayName("Deve lançar exceção quando pagamento não for encontrado por ID")
        void deveLancarExcecaoQuandoPagamentoNaoForEncontradoPorId() {
            UUID paymentId = UUID.randomUUID();

            when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

            NoSuchElementException ex = assertThrows(
                    NoSuchElementException.class,
                    () -> paymentApplicationService.findById(paymentId)
            );

            assertEquals("Pagamento nao encontrado: " + paymentId, ex.getMessage());
            verify(paymentRepository, times(1)).findById(paymentId);
        }
    }

    @Nested
    @DisplayName("Listagem de pagamentos")
    class findAll {

        @Test
        @DisplayName("Deve listar todos os pagamentos")
        void deveListarTodosOsPagamentos() {
            Payment p1 = Payment.newPayment(new BigDecimal("100.00"), PaymentMethod.PIX, "pix");
            Payment p2 = Payment.newPayment(new BigDecimal("200.00"), PaymentMethod.BOLETO, "boleto");

            when(paymentRepository.findAll()).thenReturn(List.of(p1, p2));

            List<PaymentResponse> responses = paymentApplicationService.findAll();

            assertEquals(2, responses.size());

            assertEquals(p1.id(), responses.get(0).id());
            assertEquals(p1.amount(), responses.get(0).amount());
            assertEquals(p1.method().name(), responses.get(0).method());
            assertEquals(p1.description(), responses.get(0).description());
            assertEquals(p1.status().name(), responses.get(0).status());

            assertEquals(p2.id(), responses.get(1).id());
            assertEquals(p2.amount(), responses.get(1).amount());
            assertEquals(p2.method().name(), responses.get(1).method());
            assertEquals(p2.description(), responses.get(1).description());
            assertEquals(p2.status().name(), responses.get(1).status());

            verify(paymentRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não houver pagamentos")
        void deveRetornarListaVaziaQuandoNaoHouverPagamentos() {
            when(paymentRepository.findAll()).thenReturn(List.of());

            List<PaymentResponse> responses = paymentApplicationService.findAll();

            assertTrue(responses.isEmpty());
            verify(paymentRepository, times(1)).findAll();
        }
    }
}
