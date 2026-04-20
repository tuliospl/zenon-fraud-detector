package br.com.zenon;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        System.out.println("-----------------------");
        var listTransactions = new TransactionIngestor().read("data/PS_20174392719_1491204439457_log.csv");

        listTransactions.stream().limit(10).forEach(System.out::println);

        System.out.println("-----------------------");
        var listTransactions2 = new TransactionIngestor().read("data/paysim_with_bad_data.csv");

        listTransactions2.stream().limit(10).forEach(System.out::println);
    }
}
