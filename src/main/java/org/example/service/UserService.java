package org.example.service;

import org.example.dao.UserDao;
import org.example.model.User;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class UserService {

    private UserDao userDao = new UserDao();


    public User login(String identifier, String plainPassword) {
        System.out.println("SERVICE: Intentando login para identificador: " + identifier);
        User user = null;

        user = userDao.findByEmployeeId(identifier);

        if (user == null) {
            System.out.println("SERVICE: No se encontró por ID de empleado, intentando por email...");
            user = userDao.findByEmail(identifier);
        }

        if (user == null) {
            System.out.println("SERVICE: El identificador no corresponde a ningún usuario. Login falla.");
            return null;
        }

        System.out.println("SERVICE: Usuario encontrado: " + user.getFullName());
        System.out.println("SERVICE: Verificando contraseña...");
        String storedHash = user.getPasswordHash();
        System.out.println("SERVICE: Hash recuperado de la BD:         '" + storedHash + "'");
        System.out.println("SERVICE: Longitud del hash recuperado: " + (storedHash != null ? storedHash.length() : "null"));

        boolean passwordMatches = BCrypt.checkpw(plainPassword, storedHash);

        if (passwordMatches) {
            System.out.println("SERVICE: ¡La contraseña coincide! Login exitoso.");
            return user;
        } else {
            System.out.println("SERVICE: La contraseña NO coincide. Login falla.");
            return null;
        }
    }


    public void registerUser(String fullName, String email, String plainPassword, User.Role role) {
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPasswordHash(hashedPassword);
        newUser.setRole(role);


        userDao.save(newUser);
    }



    public void createUser(String fullName, String email, String plainPassword, User.Role role) {

        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPasswordHash(hashedPassword);
        newUser.setRole(role);
        userDao.save(newUser);
    }


    public List<User> findAllUsers() {
        return userDao.findAll();
    }

    public void updateUser(User user) {

        userDao.update(user);
    }

    public void deleteUser(int userId) {

        userDao.delete(userId);
    }

}