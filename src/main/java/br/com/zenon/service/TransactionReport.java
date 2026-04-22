package br.com.zenon.service;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public class TransactionReport {

    private record ReportTransaction(BigDecimal amount, boolean isFraud) {}

    public record Statistics(Long totalTransactions, Long totalFrauds, BigDecimal totalAmount) {
        private static final Statistics ZERO = new Statistics(0L, 0L, BigDecimal.ZERO);

        private Statistics addReportTransactions(ReportTransaction rt) {
            return new Statistics(
                totalTransactions + 1L,
                totalFrauds + (rt.isFraud ? 1L : 0L),
                totalAmount.add(rt.amount));
        }

        private Statistics add(Statistics other) {
            return new Statistics(
                totalTransactions + other.totalTransactions,
                totalFrauds + other.totalFrauds,
                totalAmount.add(other.totalAmount));
        }
    }


    public Statistics generateReport(String fileName) {
        Path path = Path.of(fileName);
        try(Stream<String> lines = Files.lines(path)) {
            return lines
                .skip(1)
                .map(this::parseReportTransaction)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(Statistics.ZERO,Statistics::addReportTransactions, Statistics::add);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Optional<ReportTransaction> parseReportTransaction(String line) {
        String[] chunks = line.split(",");

        var amount = new BigDecimal(chunks[2]);
        var isFraud = "1".equals(chunks[9]);

        return Optional.of(new ReportTransaction(amount,isFraud));
    }
}
