package com.clairvoyant.stcokbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StockPrice {

  @Id
  private String id;
  private String stockName;
  private String companyName;
  private String companyInfo;
  private Double currentPrice;
  private Double openPrice;
  private Double closePrice;
  private Double highPrice;
  private Double lowPrice;
  private Double change;
}
