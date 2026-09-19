package org.example.dao;

import org.example.config.DatabaseConnection;
import org.example.model.User;

import java.sql.*;

public class UserDao {
    public boolean save(User user) {
        String sql = """
                INSERT INTO users(full_name, phone_number, pin)
                VALUES(?,?,?)
                """;

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            )
        ){
            statement.setString(1, user.getFullName());
            statement.setString(2, user.getPhoneNumber());
            statement.setInt(3, user.getPin());

            int rowInserted = statement.executeUpdate();

            if(rowInserted == 0){
                return false;
            }
            try(ResultSet generatedKeys = statement.getGeneratedKeys()){
                if(generatedKeys.next()){
                    user.setId(generatedKeys.getLong(1));
                }
            }
            return true;
        }catch(SQLException e){
            System.out.println("Error saving user: " + e.getMessage());
            return false;
        }
    }
    // Find a user by using their phone number
    public User findByPhoneNumber(String phoneNumber){
        String sql = """
                SELECT id, full_name, phone_number, pin 
                FROM users
                WHERE phone_number = ?
                """;
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, phoneNumber);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    User user = new User();

                    user.setId(resultSet.getLong("id"));
                    user.setFullName(resultSet.getString("full_name"));
                    user.setPhoneNumber(resultSet.getString("phone_number"));
                    user.setPin(resultSet.getInt("pin"));
                    return user;
                }
            }
        }catch(SQLException e){
            System.out.println("Error finding user: " + e.getMessage());
        }
        return null;
    }
    // find a user using their ID
    public User findById(long id){
        String sql = """
                SELECT id, full_name, phone_number, pin
                FROM users
                WHERE id = ?
                """;

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1, id);

            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    User user = new User();

                    user.setId(resultSet.getLong("id"));
                    user.setFullName(resultSet.getString("full_name"));
                    user.setPhoneNumber(resultSet.getString("phone_number"));
                    user.setPin(resultSet.getInt("pin"));

                    return user;
                }
            }

        }catch(SQLException e){
            System.out.println("Error finding user: " + e.getMessage());
        }
        return null;
    }
    // check whether a phone number is already registerd

    public boolean existByPhoneNumber(String phoneNumber){
        String sql = """
                SELECT 1 
                FROM users
                WHERE phone_number = ?
                """;

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, phoneNumber);

            try(ResultSet resultSet = statement.executeQuery()){
                return resultSet.next();
            }
        }catch(SQLException e){
            System.out.println("Error Checking phone Number: " + e.getMessage());
            return false;
        }
    }
}
