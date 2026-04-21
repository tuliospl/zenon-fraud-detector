package br.com.zenon;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TransactionSQLRepository implements TransactionRepository{

    public void save(Transaction transaction) {
        String sql = """
            INSERT INTO transactions(
        	    step, type, amount, name_origin, old_balance_origin, new_balance_origin,
                name_recipient, old_balance_recipient, new_balance_recipient, is_fraud, is_flagged_fraud
            ) values(?,?,?,?,?,?,?,?,?,?,?)
        """;

        try ( Connection conn = ConnectionFactory.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, transaction.step());
            ps.setString(2, transaction.type().name());
            ps.setBigDecimal(3, transaction.amount());
            ps.setString(4, transaction.origin().name());
            ps.setBigDecimal(5, transaction.origin().oldBalance());
            ps.setBigDecimal(6, transaction.origin().newBalance());
            ps.setString(7, transaction.recipient().name());
            ps.setBigDecimal(8, transaction.recipient().oldBalance());
            ps.setBigDecimal(9, transaction.recipient().newBalance());
            ps.setBoolean(10, transaction.isFraud());
            ps.setBoolean(11, transaction.isFlaggedFraud());

            ps.execute();

        } catch (SQLException ex) {
            throw new RuntimeException("Erros ao salvar nova transaçao", ex);
        }
    }

    public void saveAll(List<Transaction> transactions) {
        String sql = """
            INSERT INTO transactions(
        	    step, type, amount, name_origin, old_balance_origin, new_balance_origin,
                name_recipient, old_balance_recipient, new_balance_recipient, is_fraud, is_flagged_fraud
            ) values(?,?,?,?,?,?,?,?,?,?,?)
        """;

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);

            int count = 0;
            try(PreparedStatement ps = conn.prepareStatement(sql);) {
                for (Transaction transaction : transactions) {

                ps.setInt(1, transaction.step());
                ps.setString(2, transaction.type().name());
                ps.setBigDecimal(3, transaction.amount());
                ps.setString(4, transaction.origin().name());
                ps.setBigDecimal(5, transaction.origin().oldBalance());
                ps.setBigDecimal(6, transaction.origin().newBalance());
                ps.setString(7, transaction.recipient().name());
                ps.setBigDecimal(8, transaction.recipient().oldBalance());
                ps.setBigDecimal(9, transaction.recipient().newBalance());
                ps.setBoolean(10, transaction.isFraud());
                ps.setBoolean(11, transaction.isFlaggedFraud());

                ps.addBatch();
                count++;

                if (count % 1000 == 0) {
                    ps.executeBatch();
                    conn.commit();
                    IO.println("Salvando transacoes JDBC: " + count);
                }
            }
                IO.println("Executando batch final JDBC");
                ps.executeBatch();
                conn.setAutoCommit(true);
            }catch (SQLException e) {
                try {
                    conn.rollback();
                }catch (SQLException ex) {
                    throw new RuntimeException("Error em rollback", ex);
                }
                throw new RuntimeException("Error ao salvar a transaçao", e);
            }
        }catch (SQLException ex) {
            throw new RuntimeException("Error ao salvar transacoes", ex);
        }
    }

    @Override
    public Optional<Transaction> findByOriginName(String originName) {
        String sql = """
            SELECT id, step, `type`, amount, name_origin, old_balance_origin,
                    new_balance_origin, name_recipient, old_balance_recipient,
                    new_balance_recipient, is_fraud, is_flagged_fraud
            FROM zenon_frauds.transactions
            WHERE name_origin = ?
            ORDER BY step
            LIMIT 1
            """;

        try ( Connection conn = ConnectionFactory.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, originName);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Transaction transaction = mapResultSetToTransaction(rs);
                    return Optional.of(transaction);
                } else {
                    IO.println("Nenhum resultado encontrado");
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error ao buscar transaçao pelo nome: " + originName, ex);
        }
        return Optional.empty();
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) {

        try {
            int step = rs.getInt("step");
            TransactionType type = TransactionType.valueOf(rs.getString("type"));
            BigDecimal amount = rs.getBigDecimal("amount");
            String nameOrigin = rs.getString("name_origin");
            BigDecimal oldBalanceOrigin = rs.getBigDecimal("old_balance_origin");
            BigDecimal newBalanceOrigin = rs.getBigDecimal("new_balance_origin");
            String nameRecipient = rs.getString("name_recipient");
            BigDecimal recipientOldBalance = rs.getBigDecimal("old_balance_recipient");
            BigDecimal recipientNewBalance = rs.getBigDecimal("new_balance_recipient");
            boolean isFraud = rs.getBoolean("is_fraud");
            boolean isFlaggedFraud = rs.getBoolean("is_flagged_fraud");

            var origin = new TransactionCustomer(nameOrigin, oldBalanceOrigin, newBalanceOrigin);
            var recipient = new TransactionCustomer(nameRecipient, recipientOldBalance, recipientNewBalance);

            return new Transaction(step, type, amount, origin, recipient, isFraud, isFlaggedFraud);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
