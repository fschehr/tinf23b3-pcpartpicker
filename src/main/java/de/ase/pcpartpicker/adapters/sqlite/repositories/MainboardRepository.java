package de.ase.pcpartpicker.adapters.sqlite.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.domain.Mainboard;
import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.MotherboardFormFactor;
import de.ase.pcpartpicker.domain.HelperClasses.Socket;

public class MainboardRepository extends BaseRepository<Mainboard> {
    /**
     * Repository für die Mainboard-Komponente.
     * @author Fabio
    */
    public MainboardRepository(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public List<Mainboard> findAll() {
        String sql = """
            SELECT mb.id,
                   mb.name,
                   mb.price,
                   t.id AS type_id,
                   t.name AS type_name,
                   m.id AS manufacturer_id,
                   m.name AS manufacturer_name,
                   s.id AS socket_id,
                   s.name AS socket_name,
                   ff.id AS form_factor_id,
                   ff.name AS form_factor_name
            FROM mainboard mb
            JOIN type t ON t.id = mb.type_id
            JOIN manufacturer m ON m.id = mb.manufacturer_id
            JOIN sockets s ON s.id = mb.socket_id
            JOIN motherboard_form_factor ff ON ff.id = mb.form_factor_id
            ORDER BY mb.id
            """;
        List<Mainboard> mainboards = new ArrayList<>();

        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Manufacturer manufacturer = new Manufacturer(resultSet.getInt("manufacturer_id"), resultSet.getString("manufacturer_name"));
                Socket socket = new Socket(resultSet.getInt("socket_id"), resultSet.getString("socket_name"));
                MotherboardFormFactor formFactor = new MotherboardFormFactor(resultSet.getInt("form_factor_id"), resultSet.getString("form_factor_name"));

                mainboards.add(new Mainboard(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    manufacturer,
                    socket,
                    formFactor
                ));
            }
            return mainboards;

        } catch (SQLException e) {
            throw new IllegalStateException("Mainboard-Daten konnten nicht geladen werden.", e);
        }
    }
}