package com.clairvoyant.stcokbackend.controller;

import com.clairvoyant.stcokbackend.model.Stock;
import com.clairvoyant.stcokbackend.repository.StockRepository;
import java.time.Duration;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StocksController {

  private final StockRepository stockRepository;

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

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Stock> create(@RequestBody Stock stock) {
    return stockRepository
        .save(stock);
  }

  @PutMapping("{id}")
  public Mono<ResponseEntity<Stock>> update(@PathVariable String name,
      @RequestBody Stock stock) {
    return stockRepository
        .findById(name)
        .flatMap(existingStock -> {
          existingStock.setStockName(stock.getStockName());
          return stockRepository
              .save(existingStock);
        })
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @DeleteMapping("{id}")
  public Mono<ResponseEntity<Void>> delete(@PathVariable(value = "id") String name) {
    return stockRepository
        .findById(name)
        .flatMap(existingStock ->
            stockRepository
                .delete(existingStock)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
        )
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @GetMapping(value = "/ticker-price/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Stock> streamTickerPrice() {
    final Random random = new Random();
    return Flux.interval(Duration.ofSeconds(2))
        .map(pulse -> {
          Stock stock = new Stock();
          stock.setOpenPrice(random.nextDouble());
          return stock;
        });
  }
}
