package org.example.dao;

import org.example.model.Agent;
import org.example.model.ManagementUnit;
import org.example.model.User;
import org.example.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AgentDao {

    private UserDao userDao = new UserDao();
    private ManagementUnitDao managementUnitDao = new ManagementUnitDao();


    public Agent findById(int agentId) {
        String sql = "SELECT * FROM agents WHERE agent_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, agentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToAgent(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Agent> findAll() {
        List<Agent> agents = new ArrayList<>();
        String sql = "SELECT * FROM agents";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                agents.add(mapRowToAgent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agents;
    }

    private Agent mapRowToAgent(ResultSet rs) throws SQLException {
        Agent agent = new Agent();
        agent.setAgentId(rs.getInt("agent_id"));
        agent.setEmployeeNumber(rs.getString("employee_number"));
        agent.setApproverName(rs.getString("approver_name"));
        agent.setWorkdayId(rs.getString("workday_id"));

        int userId = rs.getInt("user_id");
        User user = userDao.findById(userId);
        agent.setUser(user);

        int muId = rs.getInt("mu_id");
        ManagementUnit mu = managementUnitDao.findById(muId);
        agent.setManagementUnit(mu);

        return agent;
    }

    public List<Agent> findByMu(int muId) {
        List<Agent> agents = new ArrayList<>();
        String sql = "SELECT * FROM agents WHERE mu_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, muId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    agents.add(mapRowToAgent(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agents;
    }
}