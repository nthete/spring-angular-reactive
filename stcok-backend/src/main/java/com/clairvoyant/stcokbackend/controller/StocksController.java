package com.clairvoyant.stcokbackend.controller;

import com.clairvoyant.stcokbackend.model.Stock;
import com.clairvoyant.stcokbackend.repository.StockRepository;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.util.Precision;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
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

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@EnableScheduling
public class StocksController {

  private final StockRepository stockRepository;
  private final String name = "MSFT";
  private List<Stock> updateList = new ArrayList<>();
  private Random random = new Random();

  @PostConstruct
  public void populateData() {
      List<Stock> list = new ArrayList();

      list.add(new Stock("MSFT", "Microsoft Corporation", "Technology",70.00, 70.00, 77.0, 81.0, 68.0, 0.00));
      list.add(new Stock("TSLA", "Tesla, Inc.", "Automotive", 53.00, 53.00, 55.0, 66.0, 51.0, 0.00));
      list.add(new Stock("SPCE", "Virgin Galactic", "Aerospace", 70.00, 70.00, 73.0, 83.0, 65.0, 0.00));
      list.add(new Stock("AAPL", "Apple, Inc.", "Technology", 93.00, 93.00, 09.0, 104.0, 91.0, 0.00));
      list.add(new Stock("GOOGL", "Alphabat", "MCC", 86.00, 86.00, 88.0, 101.0, 82.0, 0.00));

      this.updateList = list;
      stockRepository.saveAll(list);
  }

  @Scheduled(initialDelay = 0, fixedRate = 5000)
  public void updateService() {
    updateList.forEach(stock -> {
        boolean randomBoolean = random.nextBoolean();
        double number = Precision.round(random.nextDouble() * .9, 2);
        if (randomBoolean) {
            stock.setCurrentPrice(stock.getCurrentPrice() + number);
            stock.setChange(0 + number);
        } else {
            stock.setCurrentPrice(stock.getCurrentPrice() - number);
            stock.setChange(0 - number);
        }

        if (stock.getCurrentPrice() > stock.getHighPrice()) {
            stock.setHighPrice(stock.getCurrentPrice());
        } else if (stock.getCurrentPrice() < stock.getLowPrice()) {
            stock.setLowPrice(stock.getCurrentPrice());
        }
    });
  }

  @GetMapping
  public Flux<Stock> getAll() {
    return Flux.fromIterable(updateList);
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
    return Flux.fromIterable(updateList);
  }
}
