package com.clairvoyant.stcokbackend;

import com.clairvoyant.stcokbackend.model.Stock;
import com.clairvoyant.stcokbackend.model.StockPrice;
import com.clairvoyant.stcokbackend.repository.StockPriceRepository;
import com.clairvoyant.stcokbackend.repository.StockRepository;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class StockBackendApplication {

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	ReactiveMongoTemplate mongoTemplate;

	@Autowired
	private StockPriceRepository stockPriceRepository;

	public static void main(String[] args) {
		SpringApplication.run(StockBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StockRepository repository) {
		mongoTemplate
				.createCollection(StockPrice.class, CollectionOptions.empty().capped().maxDocuments(50).size(9500))
				.subscribe();
		return args -> {
			repository
					.deleteAll()
					.subscribe(null, null, () -> {
						Flux.interval(Duration.ofSeconds(1))
								.take(5)
								.map(this::getStock)
								.map(record -> repository.save(record)
										.subscribe(System.out::println))
								.subscribe();
					});
		};
	}

	private Stock getStock(long i) {
		List<Stock> list = new ArrayList();

		list.add(new Stock("MSFT", "Microsoft Corporation", "Technology",70.00, 70.00, 77.0, 81.0, 68.0, 0.00));
		list.add(new Stock("TSLA", "Tesla, Inc.", "Automotive", 53.00, 53.00, 55.0, 66.0, 51.0, 0.00));
		list.add(new Stock("SPCE", "Virgin Galactic", "Aerospace", 70.00, 70.00, 73.0, 83.0, 65.0, 0.00));
		list.add(new Stock("AAPL", "Apple, Inc.", "Technology", 93.00, 93.00, 09.0, 104.0, 91.0, 0.00));
		list.add(new Stock("GOOGL", "Alphabat", "MCC", 86.00, 86.00, 88.0, 101.0, 82.0, 0.00));

		return list.get((int) i);
	}

	private Random random = new Random();

	@Scheduled(initialDelay = 10000, fixedRate = 1000)
	public void updateService() {
			boolean randomBoolean = random.nextBoolean();
		stockRepository.findAll().subscribe(stock -> {
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
			StockPrice stockPrice = new StockPrice();
			BeanUtils.copyProperties(stock, stockPrice);
			stockPriceRepository.save(stockPrice).subscribe();
		});
	}
}
