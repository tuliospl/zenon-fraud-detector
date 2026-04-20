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

        IO.println("-------------------------------------------");

        TransactionRepository transactionRepository;

        transactionRepository = new TransactionListRepository(transactions);
        String notfoundOriginName = "C123456";
        transactionRepository.findByOriginName(notfoundOriginName)
            .ifPresentOrElse(IO::println, () -> IO.println("Transaçao nao encontrada " + notfoundOriginName));

        String existingOriginName = "C1868032458";
        long startTimeList = System.nanoTime();
        transactionRepository.findByOriginName(existingOriginName)
            .ifPresentOrElse(IO::println, () -> IO.println("Transaçao nao encontrada " + existingOriginName));
        long endTimeList = System.nanoTime();
        IO.println("Tempo de busca List: " + ((endTimeList - startTimeList) / 1_000_000.0) + "ms");

        transactionRepository = new TransactionMapRepository(transactions);
        long startTimeMap = System.nanoTime();
        transactionRepository.findByOriginName(existingOriginName)
            .ifPresentOrElse(IO::println, () -> IO.println("Transaçao nao encontrada " + existingOriginName));
        long endTimeMap = System.nanoTime();
        IO.println("Tempo de busca map: " + ((endTimeMap - startTimeMap) / 1_000_000.0) + "ms");

        IO.println("-------------------------------------------");



    }
}
