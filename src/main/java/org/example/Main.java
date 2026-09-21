package org.example;

import org.example.config.DatabaseConnection;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try(Connection connection = DatabaseConnection.getConnection()){
            System.out.println("Database connected successfully");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}