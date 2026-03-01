package de.ase.pcpartpicker.adapters.sqlite.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.domain.Ram;

public class RamRepository extends BaseRepository<Ram> {
    /**
     * Repository für die RAM-Komponente.
     * @author Fabio
    */
    public RamRepository(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public List<Ram> findAll() {
        String sql = "SELECT id, name, capacity_gb, speed_mhz, price FROM ram ORDER BY id";
        List<Ram> ramModules = new ArrayList<>();

        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                ramModules.add(new Ram(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getInt("capacity_gb"),
                    resultSet.getInt("speed_mhz"),
                    resultSet.getDouble("price")
                ));
            }
            return ramModules;

        } catch (SQLException e) {
            throw new IllegalStateException("RAM-Daten konnten nicht geladen werden.", e);
        }
    }
}