package com.banco.pagamento.adapters.in.web.dto;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error
) {
}
