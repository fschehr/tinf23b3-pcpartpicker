package de.ase.pcpartpicker.adapters.sqlite.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.domain.HelperClasses.User;

public class UserRepository extends JdbcRepository<User> {

    public UserRepository(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public List<User> findAll() {
        String sql = """
            SELECT id, name
            FROM users
            ORDER BY id
            """;

        return queryList(sql, this::mapUser, "User-Daten konnten nicht geladen werden.");
    }

    public User findById(int id) {
        String sql = """
            SELECT id, name
            FROM users
            WHERE id = ?
            """;

        return queryOptional(
            sql,
            statement -> statement.setInt(1, id),
            this::mapUser,
            "User konnte nicht geladen werden."
        ).orElse(null);
    }

    public User save(String name) {
        String sql = """
            INSERT INTO users (name)
            VALUES (?)
            """;

        int generatedId = insertAndReturnGeneratedKey(
            sql,
            statement -> statement.setString(1, name),
            "User konnte nicht gespeichert werden."
        );
        return new User(generatedId, name);
    }

    private User mapUser(ResultSet resultSet) throws SQLException {
        return new User(
            resultSet.getInt("id"),
            resultSet.getString("name")
        );
    }

    public int findUserIdByComputerId(int computerId) {
        String sql = "SELECT user_id FROM computer WHERE id = ?";
        return queryOptional(
            sql,
            statement -> statement.setInt(1, computerId),
            rs -> rs.getInt("user_id"),
            "User-ID konnte über Computer-ID nicht geladen werden."
        ).orElse(-1);
    }

    public User findByComputerId(int computerId) {
        String sql = "SELECT u.id, u.name FROM users u JOIN computer c ON c.user_id = u.id WHERE c.id = ?";
        return queryOptional(
            sql,
            statement -> statement.setInt(1, computerId),
            this::mapUser,
            "User konnte über Computer-ID nicht geladen werden."
        ).orElse(null);
    }
}
