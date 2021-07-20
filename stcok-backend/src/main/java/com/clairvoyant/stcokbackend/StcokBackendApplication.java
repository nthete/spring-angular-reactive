package com.clairvoyant.stcokbackend;

import com.clairvoyant.stcokbackend.model.Stock;
import com.clairvoyant.stcokbackend.repository.StockRepository;
import java.time.Duration;
import java.util.UUID;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class StcokBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(StcokBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StockRepository repository) {
		return args -> {
			repository
					.deleteAll()
					.subscribe(null, null, () -> {
						Flux.interval(Duration.ofSeconds(1))
								.take(11)
								.map(i -> i.intValue() + 1)
								.map(i -> {
									Stock s = new Stock();
									s.setStockName("Stock Name "+i);
									s.setCompanyName("Company Name " + i);
									s.setCompanyInfo("Company Info "+ i);
									s.setClosePrice(i + 1.50);
									s.setHighPrice(i + 1.50);
									s.setLowPrice(i + 1.50);
									s.setOpenPrice(i + 1.50);
									return s;
								})
								.map(record -> repository.save(record)
										.subscribe(System.out::println))
								.subscribe();
					});
		};
	}

}
