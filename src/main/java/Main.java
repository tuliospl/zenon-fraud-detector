import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        System.out.println(new Transaction(
            1,
            Transaction.Type.PAYMENT,
            new BigDecimal("9839.64"),
            "C1231006815",
            new BigDecimal("170136.0"),
            new BigDecimal("160296.36"),
            "M1979787155",
            new BigDecimal("0.0"),
            new BigDecimal("0.0"),
            false,
            true
        ));

        System.out.println(new Transaction(
            2,
            Transaction.Type.CASH_OUT,
            new BigDecimal("850002.52"),
            "C1280323807",
            new BigDecimal("850002.52"),
            new BigDecimal("0.0"),
            "C873221189",
            new BigDecimal("6510099.11"),
            new BigDecimal("7360101.63"),
            true,
            false
        ));
    }
}
