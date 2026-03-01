package de.ase.pcpartpicker.adapters.sqlite.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.domain.Gpu;

public class GpuRepository extends BaseRepository<Gpu> {
    /**
     * Repository für die GPU-Komponente.
     * @author Fabio
    */
    public GpuRepository(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public List<Gpu> findAll() {
        String sql = "SELECT id, name, vram_gb, price FROM gpu ORDER BY id";
        List<Gpu> gpus = new ArrayList<>();

        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                gpus.add(new Gpu(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getInt("vram_gb"),
                    resultSet.getDouble("price")
                ));
            }
            return gpus;

        } catch (SQLException e) {
            throw new IllegalStateException("GPU-Daten konnten nicht geladen werden.", e);
        }
    }
}