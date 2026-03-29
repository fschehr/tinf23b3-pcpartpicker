package de.ase.pcpartpicker.adapters.sqlite.repositories;

import java.util.List;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.domain.SSD;
import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;

public class SsdRepository extends BaseRepository<SSD> {
    /**
     * Repository für die SSD-Komponente.
     * @author Fabio
    */
    public SsdRepository(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public List<SSD> findAll() {
        String sql = """
            SELECT s.id,
                   s.name,
                   s.price,
                   s.capacity_gb,
                   t.id AS type_id,
                   t.name AS type_name,
                   m.id AS manufacturer_id,
                   m.name AS manufacturer_name
            FROM ssd s
            JOIN type t ON t.id = s.type_id
            JOIN manufacturer m ON m.id = s.manufacturer_id
            ORDER BY s.id
            """;

        return queryList(
            sql,
            resultSet -> {
                Manufacturer manufacturer = new Manufacturer(resultSet.getInt("manufacturer_id"), resultSet.getString("manufacturer_name"));

                return new SSD(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    manufacturer,
                    resultSet.getInt("capacity_gb")
                );
            },
            "SSD-Daten konnten nicht geladen werden."
        );
    }
}
