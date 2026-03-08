package de.ase.pcpartpicker.adapters.sqlite.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.domain.Case;
import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.MotherboardFormFactor;
import de.ase.pcpartpicker.domain.HelperClasses.PSUFormFactor;

public class CaseRepository extends BaseRepository<Case> {

    public CaseRepository(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public List<Case> findAll() {
        String sql = """
            SELECT c.id,
                   c.name,
                   c.price,
                   t.id AS type_id,
                   t.name AS type_name,
                   m.id AS manufacturer_id,
                   m.name AS manufacturer_name,
                 mff.id AS motherboard_form_factor_id,
                 mff.name AS motherboard_form_factor_name,
                   ff.id AS psu_form_factor_id,
                   ff.name AS psu_form_factor_name
            FROM pc_case c
            JOIN type t ON t.id = c.type_id
            JOIN manufacturer m ON m.id = c.manufacturer_id
             JOIN motherboard_form_factor mff ON mff.id = c.motherboard_form_factor_id
            JOIN psu_form_factor ff ON ff.id = c.psu_form_factor_id
            ORDER BY c.id
            """;
        List<Case> cases = new ArrayList<>();

        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Manufacturer manufacturer = new Manufacturer(resultSet.getInt("manufacturer_id"), resultSet.getString("manufacturer_name"));
                MotherboardFormFactor motherboardFormFactor = new MotherboardFormFactor(
                    resultSet.getInt("motherboard_form_factor_id"),
                    resultSet.getString("motherboard_form_factor_name")
                );
                PSUFormFactor psuFormFactor = new PSUFormFactor(resultSet.getInt("psu_form_factor_id"), resultSet.getString("psu_form_factor_name"));

                cases.add(new Case(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    manufacturer,
                    motherboardFormFactor,
                    psuFormFactor,
                    false, // hasWindow ist in der Datenbank nicht vorhanden, daher auf false gesetzt
                    0      // fanSlots ist in der Datenbank nicht vorhanden, daher auf 0
                ));
            }
            return cases;

        } catch (SQLException e) {
            throw new IllegalStateException("Case-Daten konnten nicht geladen werden.", e);
        }
    }
}