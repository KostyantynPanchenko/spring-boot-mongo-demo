package com.example.mongo.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Student {

  @Id
  private String id;
  private String name;
  private String surname;
  @Indexed(unique = true)
  private String email;
  private Gender gender;
  private Address address;
  private List<String> favourites;
  private BigDecimal totalSpentInBooks;
  private LocalDateTime createdAt;

}
