package org.example.dao;

import org.example.model.Request;
import org.example.model.Agent;
import org.example.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RequestDao {

    private AgentDao agentDao = new AgentDao();


    public List<Request> findByFilters(Request.Status status, LocalDate startDate, String agentSearchText, String lineOfWork) {
        List<Request> requests = new ArrayList<>();


        StringBuilder sql = new StringBuilder(
                "SELECT r.* FROM requests r " +
                        "JOIN agents a ON r.agent_id = a.agent_id " +
                        "JOIN users u ON a.user_id = u.user_id " +
                        "JOIN management_units mu ON a.mu_id = mu.mu_id " +
                        "WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();

        if (status != null) {
            sql.append(" AND r.status = ?");
            params.add(status.name());
        }
        if (startDate != null) {
            sql.append(" AND r.request_date >= ?");
            params.add(startDate);
        }
        if (agentSearchText != null && !agentSearchText.trim().isEmpty()) {
            sql.append(" AND (u.full_name LIKE ? OR a.employee_number LIKE ?)");
            String searchTextWithWildcards = "%" + agentSearchText.trim() + "%";
            params.add(searchTextWithWildcards);
            params.add(searchTextWithWildcards);
        }

        if (lineOfWork != null && !lineOfWork.trim().isEmpty() && !lineOfWork.equalsIgnoreCase("All")) {
            sql.append(" AND mu.line_of_work = ?");
            params.add(lineOfWork);
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapRowToRequest(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    private Request mapRowToRequest(ResultSet rs) throws SQLException {
        Request request = new Request();
        request.setRequestId(rs.getString("request_id"));
        request.setRequestDate(rs.getObject("request_date", LocalDate.class));
        request.setStatus(Request.Status.valueOf(rs.getString("status")));
        request.setRequestType(Request.RequestType.valueOf(rs.getString("request_type")));
        request.setSubmissionTimestamp(rs.getObject("submission_timestamp", Timestamp.class).toLocalDateTime());

        int agentId = rs.getInt("agent_id");
        Agent agent = agentDao.findById(agentId);
        request.setAgent(agent);


        return request;
    }
    public void save(Request request) {
        String sql = "INSERT INTO requests (request_id, agent_id, request_type, request_date, status, submission_timestamp) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, request.getRequestId());
            stmt.setInt(2, request.getAgent().getAgentId());
            stmt.setString(3, request.getRequestType().name());
            stmt.setObject(4, request.getRequestDate());
            stmt.setString(5, request.getStatus().name());
            stmt.setObject(6, request.getSubmissionTimestamp());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStatus(String requestId, Request.Status newStatus) {
        String sql = "UPDATE requests SET status = ? WHERE request_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus.name());
            stmt.setString(2, requestId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Request findById(String requestId) {
        String sql = "SELECT * FROM requests WHERE request_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, requestId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToRequest(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}