package de.ase.pcpartpicker.adapters.sqlite.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.domain.M2SSD;
import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;

public class M2SsdRepository extends BaseRepository<M2SSD> {
    /**
     * Repository für die M.2 SSD-Komponente.
     * @author Fabio
    */
    public M2SsdRepository(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public List<M2SSD> findAll() {
        String sql = """
            SELECT m2.id,
                   m2.name,
                   m2.price,
                   m2.capacity_gb,
                   t.id AS type_id,
                   t.name AS type_name,
                   m.id AS manufacturer_id,
                   m.name AS manufacturer_name
            FROM m2_ssd m2
            JOIN type t ON t.id = m2.type_id
            JOIN manufacturer m ON m.id = m2.manufacturer_id
            ORDER BY m2.id
            """;
        List<M2SSD> m2Ssds = new ArrayList<>();

        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Manufacturer manufacturer = new Manufacturer(resultSet.getInt("manufacturer_id"), resultSet.getString("manufacturer_name"));

                m2Ssds.add(new M2SSD(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    manufacturer,
                    resultSet.getInt("capacity_gb")
                ));
            }
            return m2Ssds;

        } catch (SQLException e) {
            throw new IllegalStateException("M.2 SSD-Daten konnten nicht geladen werden.", e);
        }
    }
}
