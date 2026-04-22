package br.com.zenon.model;

import java.math.BigDecimal;
import java.util.Objects;

public record TransactionCustomer(
    String name,
    BigDecimal oldBalance,
    BigDecimal newBalance
) {
    public TransactionCustomer {
        Objects.requireNonNull(name);
        Objects.requireNonNull(oldBalance);
        Objects.requireNonNull(newBalance);

        if (name.trim().isEmpty()) throw new IllegalArgumentException("O valor de name nao pode ser vazio");
        if (oldBalance.signum() < 0) throw new IllegalArgumentException("O valor de oldBalance deve ser maior ou igual a zero");
        if (newBalance.signum() < 0) throw new IllegalArgumentException("O valor de newBalance deve ser maior ou igual a zero");
    }
}
