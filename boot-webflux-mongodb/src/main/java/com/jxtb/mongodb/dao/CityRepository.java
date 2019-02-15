package com.jxtb.mongodb.dao;

import com.jxtb.mongodb.domain.City;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jxtb on 2019/2/15.
 */
@Repository
public interface CityRepository extends ReactiveMongoRepository<City, Long> {
}
