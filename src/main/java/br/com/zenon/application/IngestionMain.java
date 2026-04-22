package br.com.zenon.application;

import br.com.zenon.repository.TransactionSQLRepository;
import br.com.zenon.service.TransactionIngestorStream;

public class IngestionMain {

    void main() {
        TransactionSQLRepository transactionSQLRepository = new TransactionSQLRepository();

        long startTimerRead = System.nanoTime();
        new TransactionIngestorStream().readAsBatch(transactionSQLRepository::saveAll);
        long endTimeRead = System.nanoTime();
        IO.println("Tempo de leitura: " + ((endTimeRead - startTimerRead) / 1_000_000.0) + "ms");
    }
}
