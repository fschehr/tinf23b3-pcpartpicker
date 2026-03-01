package de.ase.pcpartpicker.adapters.sqlite.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.domain.Mainboard;

public class MainboardRepository extends BaseRepository<Mainboard> {
    /**
     * Repository für die Mainboard-Komponente.
     * @author Fabio
    */
    public MainboardRepository(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public List<Mainboard> findAll() {
        String sql = "SELECT id, name, socket, form_factor, price FROM mainboard ORDER BY id";
        List<Mainboard> mainboards = new ArrayList<>();

        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                mainboards.add(new Mainboard(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("socket"),
                    resultSet.getString("form_factor"),
                    resultSet.getDouble("price")
                ));
            }
            return mainboards;

        } catch (SQLException e) {
            throw new IllegalStateException("Mainboard-Daten konnten nicht geladen werden.", e);
        }
    }
}