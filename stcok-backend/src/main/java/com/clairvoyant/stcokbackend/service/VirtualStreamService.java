package com.clairvoyant.stcokbackend.service;

import com.clairvoyant.stcokbackend.model.Stock;
import org.apache.commons.math3.util.Precision;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class VirtualStreamService {
    private List<Stock> updateList = new ArrayList<>();
    private Random random = new Random();

    @PostConstruct
    private void populateData() {
        List<Stock> list = new ArrayList();

        list.add(new Stock("MSFT", "Microsoft Corporation", "Technology",70.00, 70.00, 77.0, 81.0, 68.0, 0.00));
        list.add(new Stock("TSLA", "Tesla, Inc.", "Automotive", 53.00, 53.00, 55.0, 66.0, 51.0, 0.00));
        list.add(new Stock("SPCE", "Virgin Galactic", "Aerospace", 70.00, 70.00, 73.0, 83.0, 65.0, 0.00));
        list.add(new Stock("AAPL", "Apple, Inc.", "Technology", 93.00, 93.00, 09.0, 104.0, 91.0, 0.00));
        list.add(new Stock("GOOGL", "Alphabat", "MCC", 86.00, 86.00, 88.0, 101.0, 82.0, 0.00));

        this.updateList = list;
    }

    @Scheduled(initialDelay = 0, fixedRate = 1000)
    private void updateService() {
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

    public Flux<Stock> getVirtualStream() {
        return Flux.fromIterable(updateList);
    }
}
