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
import de.ase.pcpartpicker.domain.HelperClasses.PsuFormFactor;
import de.ase.pcpartpicker.domain.HelperClasses.Type;

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
                   ff.id AS psu_form_factor_id,
                   ff.name AS psu_form_factor_name
            FROM pc_case c
            JOIN type t ON t.id = c.type_id
            JOIN manufacturer m ON m.id = c.manufacturer_id
            JOIN psu_form_factor ff ON ff.id = c.psu_form_factor_id
            ORDER BY c.id
            """;
        List<Case> cases = new ArrayList<>();

        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Type type = new Type(resultSet.getInt("type_id"), resultSet.getString("type_name"));
                Manufacturer manufacturer = new Manufacturer(resultSet.getInt("manufacturer_id"), resultSet.getString("manufacturer_name"));
                PsuFormFactor psuFormFactor = new PsuFormFactor(resultSet.getInt("psu_form_factor_id"), resultSet.getString("psu_form_factor_name"));

                cases.add(new Case(
                    resultSet.getInt("id"),
                    type,
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    manufacturer,
                    psuFormFactor
                ));
            }
            return cases;

        } catch (SQLException e) {
            throw new IllegalStateException("Case-Daten konnten nicht geladen werden.", e);
        }
    }
}