package br.com.zenon;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransactionIngestor {

    List<Transaction> read(String fileName) {

        List<Transaction> listTransactions = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(fileName);
        Scanner scanner = new Scanner(fis)) {
        int lineCount = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] chunks = line.split(",");
                lineCount++;

                if (lineCount == 1) {
                    continue;
                }
                if (lineCount > 1001) {
                    break;
                }

                var step = Integer.parseInt(chunks[0]);
                var type = TransactionType.valueOf(chunks[1]);
                var amount = new BigDecimal(chunks[2]);
                var origin = new TransactionCustomer(chunks[3], new BigDecimal(chunks[4]), new BigDecimal(chunks[5]));
                var recipient = new TransactionCustomer(chunks[6], new BigDecimal(chunks[7]), new BigDecimal(chunks[8]));
                var isFraud = Boolean.parseBoolean(chunks[9]);
                var isFlaggedFraud = Boolean.parseBoolean(chunks[10]);

                Transaction transaction = new Transaction(
                    step,
                    type,
                    amount,
                    origin,
                    recipient,
                    isFraud,
                    isFlaggedFraud
                );

                listTransactions.add(transaction);
            }

        }catch (Exception e) {
            throw new RuntimeException(e);
        }

        return listTransactions;
    }
}
