package br.com.zenon;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class TransactionIngestor {

    public static final int FRAUD_LIMIT = 50_000;

    List<Transaction> read() {
        Path path = Path.of("data/PS_20174392719_1491204439457_log.csv");
        try {
            List<String> lines = Files.readAllLines(path);
            return lines. stream()
            .skip(1)
            .limit(FRAUD_LIMIT)
            .map(this::parseTransaction)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Optional<Transaction> parseTransaction(String line) {
        try {
            String[] chunks = line.split(",");

            var step = Integer.parseInt(chunks[0]);
            var type = TransactionType.valueOf(chunks[1]);
            var amount = new BigDecimal(chunks[2]);
            var origin = new TransactionCustomer(chunks[3], new BigDecimal(chunks[4]), new BigDecimal(chunks[5]));
            var recipient = new TransactionCustomer(chunks[6], new BigDecimal(chunks[7]), new BigDecimal(chunks[8]));
            var isFraud = "1".equals(chunks[9]);
            var isFlaggedFraud = "1".equals(chunks[10]);

            return Optional.of(new Transaction(step, type, amount, origin, recipient, isFraud, isFlaggedFraud));
        } catch (Exception e) {
            System.err.println("Erro ao ler linha: " + line + e.getMessage());
            return Optional.empty();
        }
    }
}
