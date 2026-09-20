package org.example.dao;

import org.example.config.DatabaseConnection;
import org.example.model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {
    public boolean save(Transaction transaction) {

        String sql = """
                INSERT INTO transactions
                (account_id, type, amount, related_account)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, transaction.getAccountId());
            statement.setString(2, transaction.getType());
            statement.setBigDecimal(3, transaction.getAmount());
            statement.setString(4, transaction.getRelatedAccount());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted == 0) {
                return false;
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    transaction.setId(generatedKeys.getLong(1));
                }
            }

            return true;

        } catch (SQLException e) {
            System.out.println("Error saving transaction: "
                    + e.getMessage());

            return false;
        }
    }

    public List<Transaction> findByAccountId(long accountId) {

        List<Transaction> transactions = new ArrayList<>();

        String sql = """
                SELECT id, account_id, type,
                       amount, related_account, created_at
                FROM transactions
                WHERE account_id = ?
                ORDER BY created_at DESC
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, accountId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Transaction transaction = new Transaction();

                    transaction.setId(
                            resultSet.getLong("id"));

                    transaction.setAccountId(
                            resultSet.getLong("account_id"));

                    transaction.setType(
                            resultSet.getString("type"));

                    transaction.setAmount(
                            resultSet.getBigDecimal("amount"));

                    transaction.setRelatedAccount(
                            resultSet.getString("related_account"));

                    transaction.setCreatedAt(
                            resultSet.getTimestamp("created_at")
                                    .toLocalDateTime());

                    transactions.add(transaction);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error finding transactions: "
                            + e.getMessage());
        }

        return transactions;
    }
}
