package com.banco.pagamento;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PagamentoApplicationTests {

    @Nested
    @DisplayName("Contexto da aplicação")
    class main {

        @Test
        @DisplayName("Deve carregar o contexto da aplicação")
        void deveCarregarContextoDaAplicacao() {
        }
    }

}
