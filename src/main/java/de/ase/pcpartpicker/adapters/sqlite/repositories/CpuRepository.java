package de.ase.pcpartpicker.adapters.sqlite.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.domain.Cpu;
import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.Socket;
import de.ase.pcpartpicker.domain.HelperClasses.Type;

public class CpuRepository extends BaseRepository<Cpu> {
    /**
     * Repository für die CPU-Komponente.
     * Implementiert das BaseRepository-Interface für die CPU-Komponente.
     * @author Fabio
    */

    public CpuRepository(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }


    @Override
    public List<Cpu> findAll() {
        String sql = """
            SELECT c.id,
                   c.name,
                   c.price,
                   c.speed_ghz,
                   t.id AS type_id,
                   t.name AS type_name,
                   m.id AS manufacturer_id,
                   m.name AS manufacturer_name,
                   s.id AS socket_id,
                   s.name AS socket_name
            FROM cpu c
            JOIN type t ON t.id = c.type_id
            JOIN manufacturer m ON m.id = c.manufacturer_id
            JOIN sockets s ON s.id = c.socket_id
            ORDER BY c.id
            """;
        List<Cpu> cpus = new ArrayList<>();

        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Type type = new Type(resultSet.getInt("type_id"), resultSet.getString("type_name"));
                Manufacturer manufacturer = new Manufacturer(resultSet.getInt("manufacturer_id"), resultSet.getString("manufacturer_name"));
                Socket socket = new Socket(resultSet.getInt("socket_id"), resultSet.getString("socket_name"));

                cpus.add(new Cpu(
                    resultSet.getInt("id"),
                    type,
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    manufacturer,
                    socket,
                    resultSet.getDouble("speed_ghz")
                ));
            }
            return cpus;

        } catch (SQLException e) {
            throw new IllegalStateException("CPU-Daten konnten nicht geladen werden.", e);
        }
    }
}