package br.com.zenon;

import java.util.List;

public class DBMain {

    void main() {
        TransactionSQLRepository transactionSQLRepository = new TransactionSQLRepository();

        long startTimerRead = System.nanoTime();
        List<Transaction> transactions = new TransactionIngestor().read();
        long endTimeRead = System.nanoTime();
        IO.println("Tempo de leitura: " + ((endTimeRead - startTimerRead) / 1_000_000.0) + "ms");

        long startTimeInsert = System.nanoTime();
        transactions.forEach(transactionSQLRepository::save);
        long endTimeInsert = System.nanoTime();
        IO.println("Tempo de inserçao: " + ((endTimeInsert - startTimeInsert) / 1_000_000.0) + "ms");
    }
}
