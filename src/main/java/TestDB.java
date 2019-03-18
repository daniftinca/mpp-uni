import com.triathlon.domain.Participant;
import com.triathlon.domain.Proba;
import com.triathlon.domain.Score;
import com.triathlon.domain.User;
import com.triathlon.repository.ParticipantRepository;
import com.triathlon.repository.ProbaRepository;
import com.triathlon.repository.ScoreRepository;
import com.triathlon.repository.UserRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TestDB {


    public static void main(String[] args) {

        Properties dbProps = new Properties();

        try {
            dbProps.load(new FileReader("bd.config"));

            UserRepository userRepository = new UserRepository(dbProps);

            ProbaRepository probaRepository = new ProbaRepository(dbProps);

            ParticipantRepository participantRepository = new ParticipantRepository(dbProps);

            ScoreRepository scoreRepository = new ScoreRepository(dbProps);

            Iterable<User> arbitrii = userRepository.findAll();
            List<User> arbitriiList = new ArrayList<>();

            arbitrii.forEach(arbitriiList::add);
            User user1 = arbitriiList.get(0);
            User user2 = arbitriiList.get(1);
            User user3 = arbitriiList.get(2);
//            userRepository.save(user1);
//            userRepository.save(user2);
//            userRepository.save(user3);
//
            Participant participant1 = new Participant(20L, "Vasile");
            Participant participant2 = new Participant(21L, "Vasile Vasile");
            Participant participant3 = new Participant(22L, "Vasi");
//
            participantRepository.save(participant1);
            participantRepository.save(participant2);
            participantRepository.save(participant3);
//
            Proba proba1 = new Proba(30L, "Alergat", user1);
            Proba proba2 = new Proba(31L, "Inot", user1);
            Proba proba3 = new Proba(32L, "Ciclism", user2);
//
            probaRepository.save(proba1);
            probaRepository.save(proba2);
            probaRepository.save(proba3);

            Score score1 = new Score(40L, participant1, proba1, 10);
            Score score2 = new Score(41L, participant1, proba2, 11);
            Score score3 = new Score(42L, participant1, proba3, 12);

            Score score4 = new Score(43L, participant2, proba1, 10);
            Score score5 = new Score(44L, participant2, proba2, 11);
            Score score6 = new Score(45L, participant2, proba3, 12);

            Score score7 = new Score(46L, participant3, proba1, 10);
            Score score8 = new Score(47L, participant3, proba2, 11);
            Score score9 = new Score(48L, participant3, proba3, 12);

            scoreRepository.save(score1);
            scoreRepository.save(score2);
            scoreRepository.save(score3);
            scoreRepository.save(score4);
            scoreRepository.save(score5);
            scoreRepository.save(score6);
            scoreRepository.save(score7);
            scoreRepository.save(score8);
            scoreRepository.save(score9);


//            user2.setUsername("newUsername1");
//            userRepository.update(10L,user1);
//
//            User testUser = userRepository.findOneByUsername("newUsername1");
//
//            testUser.setName("Nume Pentru prezentare");
//
//            userRepository.update(testUser.getID(),testUser);
//
//            userRepository.delete(13L);

            // List<Score> scoresFromProba = scoreRepository.getScoresFromProba(proba1);
            //System.out.println("Something");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}