import { Component, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { Stock } from './model/stock';
import { StocksService } from './service/stocks.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'stocks';
  stocks: Observable<Stock[]>

  constructor(private stockService: StocksService) {
    this.stocks = this.stockService.getAll();
  }

  
  selectedStock: Stock | undefined;
  
  ngOnInit(){
  }
  stockClicked(stock:Stock){
      this.selectedStock = stock;
  }
}
