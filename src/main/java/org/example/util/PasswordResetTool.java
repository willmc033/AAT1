package org.example.util;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class PasswordResetTool {

    public static void main(String[] args) {

        String employeeIdToReset = "196586";
        String newPassword = "password123";

        System.out.println("Iniciando herramienta de reseteo de contraseña...");

        String newHashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        System.out.println("Nuevo hash generado para '" + newPassword + "': " + newHashedPassword);
        System.out.println("Longitud del nuevo hash: " + newHashedPassword.length());

        String sql = "UPDATE users SET password_hash = ? WHERE user_id = (SELECT user_id FROM agents WHERE employee_number = ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newHashedPassword);
            stmt.setString(2, employeeIdToReset);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("¡ÉXITO! La contraseña para el empleado " + employeeIdToReset + " ha sido actualizada en la base de datos.");
            } else {
                System.err.println("ERROR: No se encontró ningún agente con el ID " + employeeIdToReset + ". No se actualizó ninguna contraseña.");
            }

        } catch (SQLException e) {
            System.err.println("ERROR de base de datos al intentar actualizar la contraseña.");
            e.printStackTrace();
        }
    }
}