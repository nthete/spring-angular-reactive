package com.clairvoyant.stcokbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Stock {

  @Id
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
