package com.mulk.usermanagmentsystem.Dao;

import com.mulk.usermanagmentsystem.Model.User;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.*;

public class UserDao {

    private static final Logger logger = Logger.getLogger(UserDao.class);

    static {
        try {
            Class.forName("org.postgresql.Driver");
            logger.info("PostgreSQL JDBC driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            logger.error("Failed to load PostgreSQL JDBC driver.", e);
            throw new RuntimeException("Failed to load PostgreSQL JDBC driver.", e);
        }
    }

    private final String jdbcURL = "jdbc:postgresql://localhost:5432/user_management_db";
    private final String jdbcUserName = "postgres";
    private final String jdbcPasswd = "Admin1";

    //CRUD Queries
    private final String insertUserQuery = "INSERT INTO USERS (fullName,email,country) VALUES(?,?,?)";
    private final String selectUserQuery = "SELECT userId, fullName, email, country FROM USERS WHERE userId = ?";
    private final String selectAllUserQuery = "SELECT * FROM USERS";
    private final String deleteUserQuery = "DELETE FROM USERS WHERE userId = ?";
    private final String updateUserQuery = "UPDATE USERS SET fullName = ?, email = ?, country = ? WHERE userId = ?";

    //Insert user
    public Integer registerUSer(User userObj) {
        try (Connection conn = DriverManager.getConnection(jdbcURL, jdbcUserName, jdbcPasswd);
             PreparedStatement stm = conn.prepareStatement(insertUserQuery)) {

            stm.setString(1, userObj.getFullName());
            stm.setString(2, userObj.getEmail());
            stm.setString(3, userObj.getCountry());

            int num = stm.executeUpdate();
            logger.info("User registered successfully with ID: " + userObj.getUserId());
            return num;
        } catch (SQLException e) {
            logger.error("Error registering user.", e);
            return null;
        }
    }

    //updateUser
    public Integer updateUser(User userObj) {
        boolean res = false;

        try (Connection conn = DriverManager.getConnection(jdbcURL, jdbcUserName, jdbcPasswd);
             PreparedStatement stm1 = conn.prepareStatement("SELECT userId FROM USERS WHERE userId = ?")) {

            stm1.setInt(1, userObj.getUserId());

            try (ResultSet rs = stm1.executeQuery()) {
                res = rs.next();
            }

            if (res) {
                try (PreparedStatement stm = conn.prepareStatement(updateUserQuery)) {
                    stm.setString(1, userObj.getFullName());
                    stm.setString(2, userObj.getEmail());
                    stm.setString(3, userObj.getCountry());
                    stm.setInt(4, userObj.getUserId());

                    int num = stm.executeUpdate();
                    logger.info("User updated successfully with ID: " + userObj.getUserId());
                    return num;
                }
            }
        } catch (SQLException e) {
            logger.error("Error updating user.", e);
        }
        return null;
    }

    //searchUser
    public User findUserById(User userObj) {
        try (Connection conn = DriverManager.getConnection(jdbcURL, jdbcUserName, jdbcPasswd);
             PreparedStatement stm = conn.prepareStatement(selectUserQuery)) {

            stm.setInt(1, userObj.getUserId());

            try (ResultSet rs = stm.executeQuery()) {
                User theUser = new User();
                if (rs.next()) {
                    theUser.setUserId(rs.getInt("userId"));
                    theUser.setFullName(rs.getString("fullName"));
                    theUser.setEmail(rs.getString("email"));
                    theUser.setCountry(rs.getString("country"));
                    logger.info("User found with ID: " + userObj.getUserId());
                    return theUser;
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding user by ID.", e);
        }
        return null;
    }

    //deleteUser
    public Integer deleteUser(User userObj) {
        try (Connection conn = DriverManager.getConnection(jdbcURL, jdbcUserName, jdbcPasswd);
             PreparedStatement stm = conn.prepareStatement(deleteUserQuery)) {

            stm.setInt(1, userObj.getUserId());

            int num = stm.executeUpdate();
            logger.info("User deleted successfully with ID: " + userObj.getUserId());
            return num;
        } catch (SQLException e) {
            logger.error("Error deleting user.", e);
            return null;
        }
    }

    //displayAllUser
    public List<User> retrieveAllUser() {
        List<User> userList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(jdbcURL, jdbcUserName, jdbcPasswd);
             PreparedStatement stm = conn.prepareStatement(selectAllUserQuery);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                User theUser = new User();
                theUser.setUserId(rs.getInt("userId"));
                theUser.setFullName(rs.getString("fullName"));
                theUser.setEmail(rs.getString("email"));
                theUser.setCountry(rs.getString("country"));

                userList.add(theUser);
            }
            logger.info("All users retrieved successfully.");
            return userList;
        } catch (SQLException e) {
            logger.error("Error retrieving all users.", e);
            return null;
        }
    }
}
