package br.com.zenon.repository;

import br.com.zenon.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    void save(Transaction transaction);
    void saveAll(List<Transaction> transactions);
    Optional<Transaction> findByOriginName(String originName);
}
