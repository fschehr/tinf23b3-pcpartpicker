package de.ase.pcpartpicker.adapters.sqlite.repositories;

import java.util.List;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.domain.RAM;
import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;

public class RamRepository extends BaseRepository<RAM> {
    /**
     * Repository für die RAM-Komponente.
     * @author Fabio
    */
    public RamRepository(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public List<RAM> findAll() {
        String sql = """
            SELECT r.id,
                   r.name,
                   r.price,
                   r.capacity_gb,
                   r.speed_mhz,
                   t.id AS type_id,
                   t.name AS type_name,
                   m.id AS manufacturer_id,
                   m.name AS manufacturer_name
            FROM ram r
            JOIN type t ON t.id = r.type_id
            JOIN manufacturer m ON m.id = r.manufacturer_id
            ORDER BY r.id
            """;

        return queryList(
            sql,
            resultSet -> {
                Manufacturer manufacturer = new Manufacturer(resultSet.getInt("manufacturer_id"), resultSet.getString("manufacturer_name"));

                return new RAM(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    manufacturer,
                    resultSet.getInt("capacity_gb"),
                    resultSet.getInt("speed_mhz")
                );
            },
            "RAM-Daten konnten nicht geladen werden."
        );
    }
}