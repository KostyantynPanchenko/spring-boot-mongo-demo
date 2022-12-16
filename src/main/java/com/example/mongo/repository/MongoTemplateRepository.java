package com.example.mongo.repository;

import com.example.mongo.domain.Student;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MongoTemplateRepository {

  private final MongoTemplate mongo;

  public Student save(final Student student) {
    return mongo.save(student);
  }

  public Student findById(final String id) {
    return mongo.findById(id, Student.class);
  }

  public Student findOne(final String key, final String value) {
    CriteriaDefinition criteria = Criteria.where(key).is(value);
    Query selectQuery = new Query(criteria);
    return mongo.findOne(selectQuery, Student.class);
  }

  public UpdateResult updateOne(
      final String key, final String value, final String updateKey, final String updateValue) {
    Query query = new Query(Criteria.where(key).is(value));
    UpdateDefinition updateDefinition = Update.update(updateKey, updateValue);
    return mongo.updateFirst(query, updateDefinition, Student.class);
  }

  public List<Student> findMany(final String key, final String value) {
    return mongo.find(new Query(Criteria.where(key).is(value)), Student.class);
  }

  public DeleteResult deleteOne(final String key, final String value) {
    return mongo.remove(new Query(Criteria.where(key).is(value)), Student.class);
  }

  public boolean exists(final String key, final String value) {
    return mongo.exists(new Query(Criteria.where(key).is(value)), Student.class);
  }
}
