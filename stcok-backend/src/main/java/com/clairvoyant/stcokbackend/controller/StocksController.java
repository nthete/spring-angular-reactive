package com.clairvoyant.stcokbackend.controller;

import com.clairvoyant.stcokbackend.model.Stock;
import com.clairvoyant.stcokbackend.repository.StockRepository;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.clairvoyant.stcokbackend.service.VirtualStreamService;
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
  private final VirtualStreamService virtualStreamService;


  @GetMapping
  public Flux<Stock> getAll() {
    return virtualStreamService.getVirtualStream();
  }

  @GetMapping("{id}")
  public Mono<ResponseEntity<Stock>> getById(@PathVariable String name) {
    return stockRepository
        .findByStockName(name)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @GetMapping(value = "/ticker-price/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Stock> streamTickerPrice() {
    return virtualStreamService.getVirtualStream();
  }
}
