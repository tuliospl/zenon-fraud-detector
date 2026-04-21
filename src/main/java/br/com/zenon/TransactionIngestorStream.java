package br.com.zenon;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class TransactionIngestorStream {

    public static final int FRAUD_LIMIT = 10_000;
    public static final int BATCH_SIZE = 2_500;
    private final Path path = Path.of("data/PS_20174392719_1491204439457_log.csv");
    private final Semaphore semaphore = new Semaphore(100);

    void readAsBatch(Consumer<List<Transaction>> batchConsumer) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
             Stream<String> lines = Files.lines(path).skip(1)){

            var iterator = lines.iterator();

            List<String> lineBatch = new ArrayList<>(BATCH_SIZE);
            while (iterator.hasNext()) {
                String line = iterator.next();
                lineBatch.add(line);

                if (lineBatch.size() >= BATCH_SIZE) {
                    System.out.println("Salvando o batch: " + lineBatch.size());
                    List<String> currentLineBatch = List.copyOf(lineBatch);
                    executor.submit(() -> {
                        try {
                        executeBatch(currentLineBatch, batchConsumer);
                        }catch (Exception e) {
                            e.printStackTrace();
                            throw new RuntimeException("Error ao salvar o batch", e);
                        }
                    });
                    lineBatch.clear();
                }
            }

            if (!lineBatch.isEmpty()) {
                System.out.println("Executando o batch final no ingestor");
                List<String> currentLineBatch = List.copyOf(lineBatch);
                executor.submit(() -> {
                    try {
                        executeBatch(currentLineBatch, batchConsumer);
                    }catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("Error ao salvar o batch", e);
                    }
                });
            }

        } catch (Exception ex) {
            throw new RuntimeException("Error ao ler o arquivo", ex);
        }
    }

    private void executeBatch(List<String> lineBatch, Consumer<List<Transaction>> batchConsumer) {
        List<Transaction> transactionBatch =
            lineBatch
            .stream()
            .map(this::parseTransaction)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
        System.out.println("Salvando o batch: " + transactionBatch.size());
        try {
            semaphore.acquire();
            try {
                batchConsumer.accept(transactionBatch);
            } finally {
                semaphore.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    void read(Consumer<Transaction> consumer) {
        try (Stream<String> lines = Files.lines(path)){
            lines
                .skip(1)
                .limit(FRAUD_LIMIT)
                .map(this::parseTransaction)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(consumer);
        } catch (Exception ex) {
            throw new RuntimeException("Error ao ler o arquivo", ex);
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
