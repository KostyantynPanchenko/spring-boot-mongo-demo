package com.example.mongo;

import com.example.mongo.domain.Address;
import com.example.mongo.domain.Gender;
import com.example.mongo.domain.Reward;
import com.example.mongo.domain.Student;
import com.example.mongo.repository.MongoTemplateRepository;
import com.example.mongo.repository.RewardRepository;
import com.example.mongo.repository.StudentRepository;
import com.mongodb.ClientSessionOptions;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@SpringBootApplication
@Slf4j
public class SpringBootMongoApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootMongoApplication.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(
      final StudentRepository studentRepository,
      final RewardRepository rewardRepository,
      final MongoTemplate mongoTemplate,
      final MongoTemplateRepository templateRepository,
      final MongoClient mongoClient) {
    return (String[] args) -> {
      studentRepository.deleteAll();
      rewardRepository.deleteAll();
      saveJordan(studentRepository, rewardRepository, mongoTemplate);
      workWithPip(templateRepository, rewardRepository, mongoTemplate, mongoClient);
    };
  }

  private static void saveJordan(
      final StudentRepository studentRepository,
      final RewardRepository rewardRepository,
      final MongoTemplate mongoTemplate) {

    final var query = new Query();
    query.addCriteria(Criteria.where("email").is("mj23@jordan.com"));
    List<Student> students = mongoTemplate.find(query, Student.class);

    if (students.isEmpty()) {
      Reward slamDunkContestWinner = new Reward(null, "Slam Dunk contest winner",
          LocalDate.of(1985, Month.APRIL, 26));
      Reward nbaFinalsMvp = new Reward(null, "NBA Finals MVP", LocalDate.of(1995, Month.JUNE, 14));
      rewardRepository.saveAll(List.of(slamDunkContestWinner, nbaFinalsMvp));

      final var jordan = getJordan();
      jordan.getRewards().add(slamDunkContestWinner);
      jordan.getRewards().add(nbaFinalsMvp);
      log.info("Saving {}", jordan);
      studentRepository.save(jordan);
      log.info("Student {} saved", jordan);
    } else {
      log.info("Jordan already exist!");
    }
  }

  private static Student getJordan() {
    return new Student(
        null,
        "Michael",
        "Jordan",
        "mj23@jordan.com",
        Gender.MALE,
        new Address("USA", "Chicago", "Main street"),
        new LinkedList<>(),
        BigDecimal.TEN,
        LocalDateTime.now(),
        0
    );
  }

  private static void workWithPip(
      final MongoTemplateRepository templateRepository,
      final RewardRepository rewardRepository,
      final MongoTemplate mongoTemplate,
      final MongoClient client) {

    ClientSessionOptions clientSessionOptions = ClientSessionOptions.builder()
        .causallyConsistent(true)
        .build();
    ClientSession session = client.startSession(clientSessionOptions);

    mongoTemplate.withSession(session)
        .execute(action -> {
            session.startTransaction();

            try {
              Student pippen;
              Reward allStarsMvp;
              if (templateRepository.exists("name", "Scottie")) {
                pippen = templateRepository.findOne("name", "Scottie");
              } else {
                allStarsMvp = new Reward(null, "All Stars MVP",
                    LocalDate.of(1995, Month.FEBRUARY, 12));
                rewardRepository.save(allStarsMvp);
                pippen = new Student(
                    null,
                    "Scottie",
                    "Pippen",
                    "pip@pippen.com",
                    Gender.MALE,
                    new Address("USA", "Chicago", "Main street"),
                    List.of(allStarsMvp),
                    BigDecimal.TEN,
                    LocalDateTime.now(),
                    0
                );
              }

              log.info("Saved {}", templateRepository.save(pippen));
              log.info("Updated {}", templateRepository.updateOne("name", "Scottie", "email", "33@pippen.com"));
              log.info("Pip looks like {}", templateRepository.findOne("name", "Scottie"));
              log.info("All: {}", templateRepository.findMany("totalEarnings", BigDecimal.TEN));
              log.info("Removed {}", templateRepository.deleteOne("name", "Scottie"));

              session.commitTransaction();
              session.close();
            } catch (Exception exception) {
              exception.printStackTrace();
              session.abortTransaction();
            }
          return null;
        });
  }

}
