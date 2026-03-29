package de.ase.pcpartpicker.adapters.sqlite.repositories;

import java.util.List;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.domain.CPU;
import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.Socket;

public class CpuRepository extends BaseRepository<CPU> {
    /**
     * Repository für die CPU-Komponente.
     * Implementiert das BaseRepository-Interface für die CPU-Komponente.
     * @author Fabio
    */

    public CpuRepository(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public List<CPU> findAll() {
        String sql = """
            SELECT c.id,
                   c.name,
                   c.price,
                   c.speed_ghz,
                   c.hasIntegratedGraphics,
                   c.power_consumption_w,
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

        return queryList(
            sql,
            resultSet -> {
                Manufacturer manufacturer = new Manufacturer(resultSet.getInt("manufacturer_id"), resultSet.getString("manufacturer_name"));
                Socket socket = new Socket(resultSet.getInt("socket_id"), resultSet.getString("socket_name"));

                return new CPU(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    manufacturer,
                    socket,
                    resultSet.getDouble("speed_ghz"),
                    resultSet.getBoolean("hasIntegratedGraphics"),
                    resultSet.getInt("power_consumption_w")
                );
            },
            "CPU-Daten konnten nicht geladen werden."
        );
    }
}