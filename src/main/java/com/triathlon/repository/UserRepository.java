package com.triathlon.repository;


import com.triathlon.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class UserRepository implements IRepository<Long, User> {

    private static final Logger logger = LogManager.getLogger();
    private DatabaseConnector dbUtils;

    public UserRepository(Properties props) {
        logger.info("Initializing UserRepository with properties: {} ", props);
        dbUtils = new DatabaseConnector(props);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int size() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select count(*) as [SIZE] from users")) {
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    logger.traceExit(result.getInt("SIZE"));
                    return result.getInt("SIZE");
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        return 0;
    }

    @Override
    public void save(User entity) {
        logger.traceEntry("saving user {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into users values (?,?,?,?)")) {
            preStmt.setLong(1, entity.getID());
            preStmt.setString(2, entity.getUsername());
            preStmt.setString(3, entity.getPassword());
            preStmt.setString(4, entity.getName());
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Long userID) {
        logger.traceEntry("deleting user with {}", userID);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from users where ID=?")) {
            preStmt.setLong(1, userID);
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Long id, User entity) {

        logger.traceEntry("updating user with id {} ", id);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("update users set username=?, password=?,name=? where ID=?")) {
            preStmt.setString(1, entity.getUsername());
            preStmt.setString(2, entity.getPassword());
            preStmt.setString(3, entity.getName());
            preStmt.setLong(4, id);
            int rows = preStmt.executeUpdate();
            if (rows == 0) {
                logger.traceExit("No user found with id {}", id);
            } else {
                logger.traceExit("Updated user with id {}", id);
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }

    }

    @Override
    public User findOne(Long id) {
        logger.traceEntry("finding user with id {} ", id);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("select * from users where ID=?")) {
            preStmt.setLong(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    User user = getUserFromResult(result);
                    logger.traceExit(user);
                    return user;
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit("No user found with id {}", id);

        return null;
    }

    public User findOneByUsername(String username) {
        logger.traceEntry("finding user with username {} ", username);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("select * from users where username=?")) {
            preStmt.setString(1, username);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    User user = getUserFromResult(result);
                    logger.traceExit(user);
                    return user;
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit("No user found with username {}", username);

        return null;
    }

    /**
     * Gets a user object from a result
     *
     * @param result
     * @return
     * @throws SQLException
     */
    private User getUserFromResult(ResultSet result) throws SQLException {
        Long userId = result.getLong("ID");
        String username = result.getString("username");
        String passw = result.getString("password");
        String name = result.getString("name");
        return new User(userId, username, passw, name);
    }

    @Override
    public Iterable<User> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from users")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    User user = getUserFromResult(result);
                    users.add(user);
                }
                return users;
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit(users);
        return users;
    }
}
