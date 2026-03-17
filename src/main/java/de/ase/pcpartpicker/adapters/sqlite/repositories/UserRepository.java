package de.ase.pcpartpicker.adapters.sqlite.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.domain.HelperClasses.User;

public class UserRepository {

    private final ConnectionFactory connectionFactory;

    public UserRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public List<User> findAll() {
        String sql = """
            SELECT id, name
            FROM users
            ORDER BY id
            """;
        List<User> users = new ArrayList<>();

        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(new User(
                    resultSet.getInt("id"),
                    resultSet.getString("name")
                ));
            }
            return users;

        } catch (SQLException e) {
            throw new IllegalStateException("User-Daten konnten nicht geladen werden.", e);
        }
    }

    public User findById(int id) {
        String sql = """
            SELECT id, name
            FROM users
            WHERE id = ?
            """;

        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return new User(
                    resultSet.getInt("id"),
                    resultSet.getString("name")
                );
            }

        } catch (SQLException e) {
            throw new IllegalStateException("User konnte nicht geladen werden.", e);
        }
    }

    public User save(String name) {
        String sql = """
            INSERT INTO users (name)
            VALUES (?)
            """;

        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, name);
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new User(generatedKeys.getInt(1), name);
                }
            }
            throw new IllegalStateException("User konnte nicht gespeichert werden: Keine ID zurückgegeben.");

        } catch (SQLException e) {
            throw new IllegalStateException("User konnte nicht gespeichert werden.", e);
        }
    }
}
