package com.clairvoyant.stcokbackend.repository;

import com.clairvoyant.stcokbackend.model.Stock;
import java.util.Optional;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface StockRepository extends ReactiveMongoRepository<Stock, String> {

  Mono<Stock> findByStockName(String name);
}
