package com.example.mongo.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
@CompoundIndex(name = "first_and_last_name", def = "{'firstName' : 1, 'lastName' : 1}")
public class Student {

  @MongoId
  private String id;
  @Field(name = "firstName")
  private String name;
  @Field(name = "lastName")
  private String surname;
  @Indexed(unique = true)
  private String email;
  private Gender gender;
  private Address address;
  @DBRef
  private List<Reward> rewards;
  @Field(targetType = FieldType.DECIMAL128)
  private BigDecimal totalEarnings;
  private LocalDateTime createdAt;
  @Version
  private int version;

}
