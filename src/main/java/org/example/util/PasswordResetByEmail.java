package org.example.util;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class PasswordResetByEmail {



    public static void main(String[] args) {

        String emailToReset = "admin@example.com";
        String newPassword = "password123";

        System.out.println("Iniciando herramienta de reseteo de contraseña para ADMIN...");

        String newHashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        System.out.println("Nuevo hash generado para '" + newPassword + "': " + newHashedPassword);

        String sql = "UPDATE users SET password_hash = ? WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newHashedPassword);
            stmt.setString(2, emailToReset);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("¡ÉXITO! La contraseña para el usuario " + emailToReset + " ha sido actualizada en la base de datos.");
            } else {
                System.err.println("ERROR: No se encontró ningún usuario con el email " + emailToReset + ".");
            }

        } catch (SQLException e) {
            System.err.println("ERROR de base de datos al intentar actualizar la contraseña.");
            e.printStackTrace();
        }
    }

}