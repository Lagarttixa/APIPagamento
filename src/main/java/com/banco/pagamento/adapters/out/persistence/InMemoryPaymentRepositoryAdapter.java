package com.banco.pagamento.adapters.out.persistence;

import com.banco.pagamento.domain.model.Payment;
import com.banco.pagamento.domain.port.out.PaymentRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryPaymentRepositoryAdapter implements PaymentRepository {
    private final ConcurrentMap<UUID, Payment> storage = new ConcurrentHashMap<>();

    @Override
    public Payment save(Payment payment) {
        storage.put(payment.id(), payment);
        return payment;
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Payment> findAll() {
        return new ArrayList<>(storage.values());
    }
}
