package com.clairvoyant.stcokbackend.repository;

import com.clairvoyant.stcokbackend.model.Stock;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface StockRepository extends ReactiveMongoRepository<Stock, String> {

  Mono<Stock> findByStockName(String name);
}
