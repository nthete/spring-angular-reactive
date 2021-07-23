package com.clairvoyant.stcokbackend.controller;

import com.clairvoyant.stcokbackend.model.StockPrice;
import com.clairvoyant.stcokbackend.repository.StockPriceRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/api/stocks-price")
@RequiredArgsConstructor
@EnableScheduling
public class StocksPriceController {

  private final StockPriceRepository stockPriceRepository;

  @GetMapping(value = "/{name}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<StockPrice> streamTickerPrice(@PathVariable String name) {
    return stockPriceRepository.findByStockName(name)
        .repeatWhen(flux -> flux.delayElements(Duration.ofSeconds(1)))
        .subscribeOn(Schedulers.boundedElastic());
  }
}
