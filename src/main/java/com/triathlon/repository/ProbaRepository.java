package com.triathlon.repository;

import com.triathlon.domain.Proba;
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

public class ProbaRepository implements IRepository<Long, Proba> {
    private static final Logger logger = LogManager.getLogger();
    Properties dbProps;
    private DatabaseConnector dbUtils;

    public ProbaRepository(Properties props) {
        logger.info("Initializing ProbaRepository with properties: {} ", props);
        dbUtils = new DatabaseConnector(props);
        dbProps = props;
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
        try (PreparedStatement preStmt = con.prepareStatement("select count(*) as [SIZE] from proba")) {
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
    public void save(Proba entity) {
        logger.traceEntry("saving proba {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into proba values (?,?,?)")) {
            preStmt.setLong(1, entity.getId());
            preStmt.setString(2, entity.getName());
            preStmt.setLong(3, entity.getArbitru().getID());
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Long id) {
        logger.traceEntry("deleting proba with {}", id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from proba where ID=?")) {
            preStmt.setLong(1, id);
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Long id, Proba entity) {

    }

    @Override
    public Proba findOne(Long id) {
        logger.traceEntry("finding proba with id {} ", id);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("select * from proba where ID=?")) {
            preStmt.setLong(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    Proba proba = getProbaFromResult(result);
                    logger.traceExit(proba);
                    return proba;
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit("No proba found with id {}", id);

        return null;
    }

    /**
     * Gets a proba object from a result
     *
     * @param result
     * @return
     * @throws SQLException
     */
    private Proba getProbaFromResult(ResultSet result) throws SQLException {
        Long id = result.getLong("ID");
        String name = result.getString("name");
        Long idArbitru = result.getLong("IDArbitru");
        UserRepository userRepository = new UserRepository(dbProps);
        User arbitru = userRepository.findOne(idArbitru);
        return new Proba(id, name, arbitru);
    }

    @Override
    public Iterable<Proba> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Proba> probe = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from proba")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Proba probaFromResult = getProbaFromResult(result);
                    probe.add(probaFromResult);
                }
                logger.traceExit(probe);
                return probe;
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit(probe);
        return probe;
    }


    public Iterable<Proba> findAllForUsername(String username) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Proba> probe = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from proba inner join users u on proba.IDArbitru = u.ID where u.username=?")) {
            preStmt.setString(1, username);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Proba probaFromResult = getProbaFromResult(result);
                    probe.add(probaFromResult);
                }
                logger.traceExit(probe);
                return probe;
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit(probe);
        return probe;
    }
}
