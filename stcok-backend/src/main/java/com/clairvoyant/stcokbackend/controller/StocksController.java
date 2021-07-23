package com.clairvoyant.stcokbackend.controller;

import com.clairvoyant.stcokbackend.model.Stock;
import com.clairvoyant.stcokbackend.repository.StockPriceRepository;
import com.clairvoyant.stcokbackend.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@EnableScheduling
public class StocksController {

  private final StockRepository stockRepository;
  private final StockPriceRepository stockPriceRepository;


  @GetMapping
  public Flux<Stock> getAll() {
    return stockRepository.findAll();
  }

  @GetMapping("{id}")
  public Mono<ResponseEntity<Stock>> getById(@PathVariable String name) {
    return stockRepository
        .findByStockName(name)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping("{id}")
  public Mono<ResponseEntity<Stock>> createStock(@RequestBody Stock stock) {
    return stockRepository
        .save(stock)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @DeleteMapping("{id}")
  public Mono<ResponseEntity<Void>> deleteById(@PathVariable String name) {
    return stockRepository
        .deleteById(name)
        .map(ResponseEntity::ok);
  }
}
