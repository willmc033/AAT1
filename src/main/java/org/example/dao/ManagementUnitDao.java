package org.example.dao;

import org.example.model.ManagementUnit;
import org.example.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManagementUnitDao {

    public ManagementUnit findById(int muId) {
        String sql = "SELECT * FROM management_units WHERE mu_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, muId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToManagementUnit(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<ManagementUnit> findAll() {
        List<ManagementUnit> units = new ArrayList<>();
        String sql = "SELECT * FROM management_units ORDER BY mu_name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                units.add(mapRowToManagementUnit(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return units;
    }


    private ManagementUnit mapRowToManagementUnit(ResultSet rs) throws SQLException {
        ManagementUnit mu = new ManagementUnit();
        mu.setMuId(rs.getInt("mu_id"));
        mu.setMuName(rs.getString("mu_name"));
        mu.setLineOfWork(rs.getString("line_of_work"));
        return mu;
    }
}