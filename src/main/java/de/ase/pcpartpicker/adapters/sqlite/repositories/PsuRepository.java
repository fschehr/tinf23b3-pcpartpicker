package de.ase.pcpartpicker.adapters.sqlite.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.domain.PSU;
import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.PsuFormFactor;
import de.ase.pcpartpicker.domain.HelperClasses.Type;

public class PsuRepository extends BaseRepository<PSU> {

    public PsuRepository(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public List<PSU> findAll() {
        String sql = """
            SELECT p.id,
                   p.name,
                   p.price,
                   p.wattage,
                   t.id AS type_id,
                   t.name AS type_name,
                   m.id AS manufacturer_id,
                   m.name AS manufacturer_name,
                   ff.id AS form_factor_id,
                   ff.name AS form_factor_name
            FROM psu p
            JOIN type t ON t.id = p.type_id
            JOIN manufacturer m ON m.id = p.manufacturer_id
            JOIN psu_form_factor ff ON ff.id = p.form_factor_id
            ORDER BY p.id
            """;
        List<PSU> psus = new ArrayList<>();

        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Type type = new Type(resultSet.getInt("type_id"), resultSet.getString("type_name"));
                Manufacturer manufacturer = new Manufacturer(resultSet.getInt("manufacturer_id"), resultSet.getString("manufacturer_name"));
                PsuFormFactor formFactor = new PsuFormFactor(resultSet.getInt("form_factor_id"), resultSet.getString("form_factor_name"));

                psus.add(new PSU(
                    resultSet.getInt("id"),
                    type,
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    manufacturer,
                    resultSet.getInt("wattage"),
                    formFactor
                ));
            }
            return psus;

        } catch (SQLException e) {
            throw new IllegalStateException("PSU-Daten konnten nicht geladen werden.", e);
        }
    }
}