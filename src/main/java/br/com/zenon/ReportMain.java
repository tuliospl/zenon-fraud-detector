package br.com.zenon;

import br.com.zenon.TransactionReport.Statistics;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReportMain {

    public static void main(String[] args) {
        String language = (args.length > 0) ? args[0] : "pt";
        var locale = Locale.of(language);

        var integerFormat = NumberFormat.getIntegerInstance(locale);
        var currencyFormatter = DecimalFormat.getCurrencyInstance(locale);
        currencyFormatter.setCurrency(Currency.getInstance("USD"));

        ResourceBundle resourceBundle = ResourceBundle.getBundle("report", locale);

        var transactionReport = new TransactionReport();
        Statistics statistics = transactionReport.generateReport("data/PS_20174392719_1491204439457_log.csv");

        String fmtTotalTransactions = integerFormat.format(statistics.totalTransactions());
        String fmtTotalFrauds = integerFormat.format(statistics.totalFrauds());
        String fmtTotalAmount = currencyFormatter.format(statistics.totalAmount());

        String msgTotalTransaction = resourceBundle.getString("label.total.transactions");
        String msgTotalFrauds = resourceBundle.getString("label.total.frauds");
        String msgTotalAmount = resourceBundle.getString("label.total.amount");

        IO.println("""
            %s: %s
            %s: %s
            %s: %s
            """.formatted(
                msgTotalTransaction, fmtTotalTransactions,
                msgTotalFrauds, fmtTotalFrauds,
                msgTotalAmount, fmtTotalAmount
            )
        );
    }
}
