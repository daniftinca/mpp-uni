package com.triathlon.repository;

import com.triathlon.domain.Participant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParticipantRepository implements IRepository<Long, Participant> {
    private static final Logger logger = LogManager.getLogger();
    private DatabaseConnector dbUtils;

    public ParticipantRepository(Properties props) {
        logger.info("Initializing ParticipantRepository with properties: {} ", props);
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
        try (PreparedStatement preStmt = con.prepareStatement("select count(*) as [SIZE] from participants")) {
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
    public void save(Participant entity) {
        logger.traceEntry("saving participant {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into participants values (?,?)")) {
            preStmt.setLong(1, entity.getId());
            preStmt.setString(2, entity.getName());
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Long id) {
        logger.traceEntry("deleting participant with {}", id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from participants where ID=?")) {
            preStmt.setLong(1, id);
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Long id, Participant entity) {
        //todo: nu trebe
    }

    @Override
    public Participant findOne(Long id) {
        logger.traceEntry("finding participant with id {} ", id);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("select * from participants where ID=?")) {
            preStmt.setLong(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    Participant participant = getParticipantFromResult(result);
                    logger.traceExit(participant);
                    return participant;
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit("No participant found with id {}", id);

        return null;
    }


    /**
     * Gets a participant object from a result
     *
     * @param result
     * @return
     * @throws SQLException
     */
    private Participant getParticipantFromResult(ResultSet result) throws SQLException {
        Long id = result.getLong("ID");
        String name = result.getString("name");
        return new Participant(id, name);
    }

    @Override
    public Iterable<Participant> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Participant> participants = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from participants")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Participant participant = getParticipantFromResult(result);
                    participants.add(participant);
                }
                return participants;
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit(participants);
        return participants;
    }
}
