package br.com.zenon;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        List<Transaction> transactions = new TransactionIngestor().read();

        FraudAnalyzer fraudAnalyzer = new FraudAnalyzer(transactions);
        Long fraudCount = fraudAnalyzer.countFrauds();
        IO.println("Total de fraudes: " + fraudCount);

        List<BigDecimal> highestValueFrauds = fraudAnalyzer.findHighestValueFraudsAmounts(3);
        IO.println("Top 3 fraudes de maior valor: ");
        highestValueFrauds.forEach(amount -> IO.println("- %.2f".formatted(amount)));

        List<String> topSuspiciousClients = fraudAnalyzer.findTopSuspiciousClients(5);
        IO.println("Top 5 clientes suspeitos: " + topSuspiciousClients);

        BigDecimal totalFraudLoss = fraudAnalyzer.calculateTotalFraudLoss();
        IO.println("Valor de prejuizo total: " + totalFraudLoss);

        Map<TransactionType, Long> fraudsByType = fraudAnalyzer.countFraudsByType();
        IO.println("Contagem de fraudes por tipo: ");
        fraudsByType.forEach((type, count) -> IO.println("- %s: %d".formatted(type, count)));
    }
}
