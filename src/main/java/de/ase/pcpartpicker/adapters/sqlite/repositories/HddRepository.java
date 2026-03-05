package de.ase.pcpartpicker.adapters.sqlite.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.domain.HDD;
import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;

public class HddRepository extends BaseRepository<HDD> {
    /**
     * Repository für die HDD-Komponente.
     * @author Fabio
    */
    public HddRepository(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public List<HDD> findAll() {
        String sql = """
            SELECT h.id,
                   h.name,
                   h.price,
                   h.capacity_gb,
                   t.id AS type_id,
                   t.name AS type_name,
                   m.id AS manufacturer_id,
                   m.name AS manufacturer_name
            FROM hdd h
            JOIN type t ON t.id = h.type_id
            JOIN manufacturer m ON m.id = h.manufacturer_id
            ORDER BY h.id
            """;
        List<HDD> hdds = new ArrayList<>();

        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Manufacturer manufacturer = new Manufacturer(resultSet.getInt("manufacturer_id"), resultSet.getString("manufacturer_name"));

                hdds.add(new HDD(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    manufacturer,
                    resultSet.getInt("capacity_gb")
                ));
            }
            return hdds;

        } catch (SQLException e) {
            throw new IllegalStateException("HDD-Daten konnten nicht geladen werden.", e);
        }
    }
}
