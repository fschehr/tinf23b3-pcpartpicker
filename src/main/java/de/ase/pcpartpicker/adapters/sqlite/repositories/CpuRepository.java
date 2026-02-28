package de.ase.pcpartpicker.adapters.sqlite.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.domain.Cpu;

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
        String sql = "SELECT id, name, socket, speed_ghz, price FROM cpu ORDER BY id";
        List<Cpu> cpus = new ArrayList<>();

        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                cpus.add(new Cpu(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("socket"),
                    resultSet.getDouble("speed_ghz"),
                    resultSet.getDouble("price")
                ));
            }
            return cpus;

        } catch (SQLException e) {
            throw new IllegalStateException("CPU-Daten konnten nicht geladen werden.", e);
        }
    }
}