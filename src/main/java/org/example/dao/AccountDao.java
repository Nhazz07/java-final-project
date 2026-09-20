package org.example.dao;

import org.example.config.DatabaseConnection;
import org.example.model.Account;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    public boolean createAccount(Account account){
        String sql = """
                INSERT INTO accounts
                (account_number, user_id, account_type, balance)
                VALUES(?,?,?,?)
                """;

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            )){
            statement.setString(1, account.getAccountNumber());
            statement.setLong(2, account.getUserId());
            statement.setString(3, account.getAccountType());
            statement.setBigDecimal(4, account.getBalance());

            int rowInserted = statement.executeUpdate();

            if(rowInserted == 0){
                return false;
            }
            try(ResultSet generatedKeys = statement.getGeneratedKeys()){
                if(generatedKeys.next()){
                    account.setId(generatedKeys.getLong(1));
                }
            }
            return true;

        }catch (SQLException e){
            System.out.println("Error creating account: " + e.getMessage());
            return false;
        }
    }

    public Account findById(long id){
        String sql = """
                SELECT id, account_number, user_id, account_type, balance
                FROM accounts
                WHERE id = ?
                """;

        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1, id);

            try(ResultSet resultSet = statement.getResultSet()){
                if(resultSet.next()){
                    return mapAccount(resultSet);
                }
            }
        }catch (SQLException e){
            System.out.println("Error finding account: " + e.getMessage());
        }
        return null;
    }

    public Account findAccountByNumber(String accountNumber){
        String sql = """
                SELECT id, account_number, user_id, account_type, balance
                FROM accounts
                WHERE account_number = ?
                """;

        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, accountNumber);

            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return mapAccount(resultSet);
                }
            }

        }catch(SQLException e){
            System.out.println("Error finding account: " + e.getMessage());
        }
        return null;
    }

    public List<Account> findByUserId(long userId){
        List<Account> accounts = new ArrayList<>();

        String sql = """
                SELECT id, account_number, user-id, account_type, balance
                FROM accounts
                WHERE user_id = ?
                """;
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1, userId);

            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    accounts.add(mapAccount(resultSet));
                }
            }

        }catch(SQLException e){
            System.out.println("Error finding accounts: " + e.getMessage());
        }
        return accounts;
    }
    public boolean updateBalance(long accountId, BigDecimal newBalance){
        String sql = """
                UPDATE accounts
                SET balance = ?
                WHERE id = ?
                """;

        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setBigDecimal(1, newBalance);
            statement.setLong(1,accountId);

            return statement.executeUpdate() > 0;
        }catch(SQLException e){
            System.out.println("Error updating balance: " + e.getMessage());
            return false;

        }
    }
    private Account mapAccount(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.setId(resultSet.getLong("id"));
        account.setAccountNumber(resultSet.getString("account_number"));
        account.setUserId(resultSet.getLong("user_id"));
        account.setAccountType(resultSet.getString("account_type"));
        account.setBalance(resultSet.getBigDecimal("balance"));

        return account;
    }
}
