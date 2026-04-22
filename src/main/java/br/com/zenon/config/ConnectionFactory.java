package br.com.zenon.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static final Properties properties = new Properties();
    
    static {
        try (InputStream input = ConnectionFactory.class.getClassLoader()
                .getResourceAsStream("properties")) {
            if (input == null) {
                throw new RuntimeException("Arquivo 'properties' não encontrado no classpath!");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar arquivo 'properties'", e);
        }
    }

    private ConnectionFactory() {}

    public static Connection getConnection() {
        try {
            String url = properties.getProperty("database.url");
            String username = properties.getProperty("database.username");
            String password = properties.getProperty("database.password");
            
            if (url == null || username == null || password == null) {
                throw new RuntimeException("Propriedades do banco de dados não configuradas no arquivo 'properties'");
            }
            
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar com o banco de dados", e);
        }
    }
}