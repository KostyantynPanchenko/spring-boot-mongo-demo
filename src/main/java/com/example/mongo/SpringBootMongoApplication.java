package com.example.mongo;

import com.example.mongo.domain.Address;
import com.example.mongo.domain.Gender;
import com.example.mongo.domain.Student;
import com.example.mongo.repository.MongoTemplateRepository;
import com.example.mongo.repository.StudentRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
      final MongoTemplate mongoTemplate,
      final MongoTemplateRepository templateRepository) {
    return (String[] args) -> {
      saveJordan(studentRepository, mongoTemplate);
      doSomeStuff(templateRepository);
    };
  }

  private static void saveJordan(StudentRepository studentRepository, MongoTemplate mongoTemplate) {
    final var student = new Student(
          null,
          "Michael",
          "Jordan",
          "mj23@jordan.com",
          Gender.MALE,
          new Address("USA", "Chicago", "Main street"),
          List.of("Victory"),
          BigDecimal.TEN,
          LocalDateTime.now()
    );

    final var query = new Query();
    query.addCriteria(Criteria.where("email").is("mj23@jordan.com"));
    List<Student> students = mongoTemplate.find(query, Student.class);

    if (students.isEmpty()) {
      log.info("Saving {}", student);
      studentRepository.save(student);
      log.info("Student {} saved", student);
    } else {
      log.info("Jordan already exist!");
    }
  }

  private void doSomeStuff(MongoTemplateRepository templateRepository) {
    Student pippen;
    if (templateRepository.exists("name", "Scottie")) {
      pippen = templateRepository.findOne("name", "Scottie");
    } else {
      pippen = new Student(
          null,
          "Scottie",
          "Pippen",
          "pip@pippen.com",
          Gender.MALE,
          new Address("USA", "Chicago", "Main street"),
          List.of("Greatest Small Forward"),
          BigDecimal.TEN,
          LocalDateTime.now()
      );
    }

    log.info("Saved {}", templateRepository.save(pippen));
    log.info("Updated {}", templateRepository.updateOne("name", "Scottie", "email", "33@pippen.com"));
    log.info("Pip looks like {}", templateRepository.findOne("name", "Scottie"));
    log.info("All: {}", templateRepository.findMany("totalSpentInBooks", "10"));
    log.info("Removed {}", templateRepository.deleteOne("name", "Scottie"));
  }

}
