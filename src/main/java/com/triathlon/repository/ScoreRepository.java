package com.triathlon.repository;


import com.triathlon.domain.Participant;
import com.triathlon.domain.Proba;
import com.triathlon.domain.Score;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ScoreRepository implements IRepository<Long, Score> {
    private static final Logger logger = LogManager.getLogger();
    Properties dbProps;
    private DatabaseConnector dbUtils;

    public ScoreRepository(Properties props) {
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
        try (PreparedStatement preStmt = con.prepareStatement("select count(*) as [SIZE] from scores")) {
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
    public void save(Score entity) {
        logger.traceEntry("saving score {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into scores values (?,?,?,?)")) {
            preStmt.setLong(1, entity.getId());
            preStmt.setLong(2, entity.getParticipant().getId());
            preStmt.setLong(3, entity.getProba().getId());
            preStmt.setInt(4, entity.getScore());
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Long id) {
        logger.traceEntry("deleting score with {}", id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from scores where ID=?")) {
            preStmt.setLong(1, id);
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Long id, Score entity) {

    }

    @Override
    public Score findOne(Long id) {
        logger.traceEntry("finding score with id {} ", id);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("select * from scores where ID=?")) {
            preStmt.setLong(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    Score score = getScoreFromResult(result);
                    logger.traceExit(score);
                    return score;
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit("No score found with id {}", id);

        return null;
    }


    public Iterable<Score> getScoresFromProba(Proba proba) {
        logger.traceEntry("finding scores for proba  {} ", proba.getName());
        Connection con = dbUtils.getConnection();


        List<Score> scores = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from participants Inner join scores s on participants.ID = s.IDParticipant inner join proba p on s.IDProba = p.ID where p.ID = ? order by s.score desc")) {
            preStmt.setLong(1, proba.getId());

            return executeScoreSelect(scores, preStmt);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit(scores);

        return scores;

    }


    /**
     * Gets entity object from a result
     *
     * @param result
     * @return
     * @throws SQLException
     */
    private Score getScoreFromResult(ResultSet result) throws SQLException {
        Long id = result.getLong("ID");
        Long participantId = result.getLong("IDParticipant");
        Long probaId = result.getLong("IDProba");
        Integer score = result.getInt("score");

        ParticipantRepository participantRepository = new ParticipantRepository(dbProps);
        Participant participant = participantRepository.findOne(participantId);

        ProbaRepository probaRepository = new ProbaRepository(dbProps);
        Proba proba = probaRepository.findOne(probaId);


        return new Score(id, participant, proba, score);
    }

    @Override
    public Iterable<Score> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Score> scores = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from scores")) {
            return executeScoreSelect(scores, preStmt);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit(scores);
        return scores;
    }


    private Iterable<Score> executeScoreSelect(List<Score> scores, PreparedStatement preStmt) throws SQLException {
        try (ResultSet result = preStmt.executeQuery()) {
            while (result.next()) {
                Score scoreFromResult = getScoreFromResult(result);
                scores.add(scoreFromResult);
            }
            logger.traceExit(scores);
            return scores;
        }
    }
}
