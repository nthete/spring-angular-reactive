package com.clairvoyant.stcokbackend.controller;

import com.clairvoyant.stcokbackend.model.Stock;
import com.clairvoyant.stcokbackend.repository.StockRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@EnableScheduling
public class StocksController {

  private final StockRepository stockRepository;


  @GetMapping
  public Flux<Stock> getAll() {
    return stockRepository.findAll();
  }

  /**
    // SPRING MVC

    @GetMapping
    public List<Stock> getAll() {
      return stockRepository.findAll();
    }

  */


  @GetMapping("/{id}")
  public Mono<ResponseEntity<Stock>> getById(@PathVariable String id) {
    return stockRepository
        .findByStockName(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  /**

   // SPRING MVC

   @GetMapping("/{id}")
   public Stock getById(@PathVariable String id) {
     return stockRepository
             .findByStockName(id);
   }
   */

  @PostMapping("/{id}")
  public Mono<ResponseEntity<Boolean>> createStock(@PathVariable String id, @RequestBody Stock stock) {
    if(!id.equalsIgnoreCase(stock.getStockName())) {
      throw new IllegalArgumentException();
    }
    return stockRepository
        .save(stock)
        .map(savedStock -> ResponseEntity.ok(Objects.nonNull(savedStock)));
  }

  @ResponseStatus(
      value = HttpStatus.BAD_REQUEST,
      reason = "Illegal arguments")
  @ExceptionHandler(IllegalArgumentException.class)
  public void illegalArgumentHandler() {
    //
  }

  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id) {
    return stockRepository
        .deleteById(id)
        .map(ResponseEntity::ok);
  }
}
