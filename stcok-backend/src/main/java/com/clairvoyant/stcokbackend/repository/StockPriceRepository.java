package com.clairvoyant.stcokbackend.repository;

import com.clairvoyant.stcokbackend.model.StockPrice;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface StockPriceRepository extends ReactiveMongoRepository<StockPrice, String> {

  @Tailable
  Flux<StockPrice> findByStockName(String name);
}
