package de.ase.pcpartpicker.adapters.sqlite.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;

/**
 * Gemeinsame JDBC-Basisklasse für Repositorys.
 *
 * @param <T> Typ des Domain-Objekts.
 */
public abstract class JdbcRepository<T> implements Repository<T> {

    @FunctionalInterface
    protected interface RowMapper<R> {
        R map(ResultSet resultSet) throws SQLException;
    }

    @FunctionalInterface
    protected interface StatementConfigurer {
        void configure(PreparedStatement statement) throws SQLException;
    }

    protected static final StatementConfigurer NO_PARAMETERS = statement -> { };

    protected final ConnectionFactory connectionFactory;

    protected JdbcRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    protected <R> List<R> queryList(String sql, RowMapper<R> rowMapper, String errorMessage) {
        return queryList(sql, NO_PARAMETERS, rowMapper, errorMessage);
    }

    protected <R> List<R> queryList(
        String sql,
        StatementConfigurer statementConfigurer,
        RowMapper<R> rowMapper,
        String errorMessage
    ) {
        List<R> results = new ArrayList<>();

        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statementConfigurer.configure(statement);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    results.add(rowMapper.map(resultSet));
                }
            }
            return results;

        } catch (SQLException e) {
            throw new IllegalStateException(errorMessage, e);
        }
    }

    protected <R> Optional<R> queryOptional(String sql, RowMapper<R> rowMapper, String errorMessage) {
        return queryOptional(sql, NO_PARAMETERS, rowMapper, errorMessage);
    }

    protected <R> Optional<R> queryOptional(
        String sql,
        StatementConfigurer statementConfigurer,
        RowMapper<R> rowMapper,
        String errorMessage
    ) {
        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statementConfigurer.configure(statement);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(rowMapper.map(resultSet));
            }

        } catch (SQLException e) {
            throw new IllegalStateException(errorMessage, e);
        }
    }

    protected int insertAndReturnGeneratedKey(
        String sql,
        StatementConfigurer statementConfigurer,
        String errorMessage
    ) {
        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statementConfigurer.configure(statement);
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
            throw new IllegalStateException(errorMessage + ": Keine ID zurückgegeben.");

        } catch (SQLException e) {
            throw new IllegalStateException(errorMessage, e);
        }
    }
}