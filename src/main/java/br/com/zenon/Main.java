package br.com.zenon;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        System.out.println(new Transaction(
            1,
            TransactionType.PAYMENT,
            new BigDecimal("9839.64"),
            new TransactionCustomer(
                "C1231006815",
                new BigDecimal("170136.0"),
                new BigDecimal("160296.36")
            ),
            new TransactionCustomer(
                "M1979787155",
                new BigDecimal("0.0"),
                new BigDecimal("0.0")
            ),
            false,
            true
        ));

        System.out.println(new Transaction(
            2,
            TransactionType.CASH_OUT,
            new BigDecimal("850002.52"),
            new TransactionCustomer(
                "C1280323807",
                new BigDecimal("850002.52"),
                new BigDecimal("0.0")
            ),
            new TransactionCustomer(
                "C873221189",
                new BigDecimal("6510099.11"),
                new BigDecimal("7360101.63")
            ),
            true,
            false
        ));

        System.out.println("-----------------------");
        var listTransactions = new TransactionIngestor().read("data/PS_20174392719_1491204439457_log.csv");

        listTransactions.stream().limit(10).forEach(System.out::println);
    }
}
