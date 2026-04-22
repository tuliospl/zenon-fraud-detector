package br.com.zenon.service;

import br.com.zenon.model.TransactionType;
import br.com.zenon.model.Transaction;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.math.BigDecimal.ZERO;

public class FraudAnalyzer {

    private final List<Transaction> transactions;

    public FraudAnalyzer(List<Transaction> transactions) {
        Objects.requireNonNull(transactions);
        this.transactions = transactions;
    }

    public Long countFrauds() {
        return fraudStream().count();
    }

    public List<BigDecimal> findHighestValueFraudsAmounts(int limit) {
        return highValuesFraudStream()
            .map(Transaction::amount)
            .limit(limit)
            .toList();
    }

    public List<String> findTopSuspiciousClients(int limit) {
        return highValuesFraudStream()
            .map(transaction -> transaction.origin().name())
            .distinct()
            .limit(limit)
            .toList();
    }

    public BigDecimal calculateTotalFraudLoss() {
        return fraudStream().map(Transaction::amount).reduce(ZERO, BigDecimal::add);
    }

    public Map<TransactionType, Long> countFraudsByType() {
        return fraudStream().collect(Collectors.groupingBy(Transaction::type, Collectors.counting()));
    }

    private Stream<Transaction> fraudStream() {
        return transactions
            .stream()
            .filter(Transaction::isFraud);
    }

    private Stream<Transaction> highValuesFraudStream() {
        return fraudStream()
            .sorted(Comparator.comparing(Transaction::amount).reversed());
    }
}
