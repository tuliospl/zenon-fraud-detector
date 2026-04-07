import java.math.BigDecimal;

public record Transaction(
    int step,
    Type type,
    BigDecimal amount,
    String nameOrig,
    BigDecimal oldBalanceOrg,
    BigDecimal newBalanceOrg,
    String nameDest,
    BigDecimal oldBalanceDest,
    BigDecimal newBalanceDest,
    boolean isFraud,
    boolean isFlaggedFraud
) {
   enum Type {
       CASH_IN, CASH_OUT, DEBIT, PAYMENT, TRANSFER
   }
}
