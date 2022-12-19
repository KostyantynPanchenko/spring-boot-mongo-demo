package com.example.mongo.repository;

import com.example.mongo.domain.Reward;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RewardRepository extends MongoRepository<Reward, ObjectId> {}
