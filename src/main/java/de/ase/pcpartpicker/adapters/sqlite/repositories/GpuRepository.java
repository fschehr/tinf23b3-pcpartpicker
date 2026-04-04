package de.ase.pcpartpicker.adapters.sqlite.repositories;

import java.util.List;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.domain.GPU;
import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;

public class GpuRepository extends BaseRepository<GPU> {
    /**
     * Repository für die GPU-Komponente.
     * @author Fabio
    */
    public GpuRepository(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public List<GPU> findAll() {
        String sql = """
            SELECT g.id,
                   g.name,
                   g.price,
                     g.core_clock,
                     g.boost_clock,
                   g.vram_gb,
                   g.power_consumption_w,
                   t.id AS type_id,
                   t.name AS type_name,
                   m.id AS manufacturer_id,
                   m.name AS manufacturer_name
            FROM gpu g
            JOIN type t ON t.id = g.type_id
            JOIN manufacturer m ON m.id = g.manufacturer_id
            ORDER BY g.id
            """;

        return queryList(
            sql,
            resultSet -> {
                Manufacturer manufacturer = new Manufacturer(resultSet.getInt("manufacturer_id"), resultSet.getString("manufacturer_name"));

                return new GPU(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    manufacturer,
                    resultSet.getDouble("core_clock"),
                    readNullableDouble(resultSet, "boost_clock"),
                    resultSet.getInt("vram_gb"),
                    resultSet.getInt("power_consumption_w")
                );
            },
            "GPU-Daten konnten nicht geladen werden."
        );
    }

    private Double readNullableDouble(java.sql.ResultSet resultSet, String columnName) throws java.sql.SQLException {
        double value = resultSet.getDouble(columnName);
        return resultSet.wasNull() ? null : value;
    }
}