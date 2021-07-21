import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Stock } from 'src/app/model/stock';
import { EventSourceService } from 'src/app/service/event-source.service';
import { StocksService } from 'src/app/service/stocks.service';
import { environment } from 'src/environments/environment';
import { map, take } from 'rxjs/operators';


@Component({
  selector: 'app-ticker-info',
  templateUrl: './ticker-info.component.html',
  styleUrls: ['./ticker-info.component.scss']
})
export class TickerInfoComponent implements OnInit, OnDestroy {

  @Input() stockInfo: Stock | undefined;

  private readonly BASE_URL = environment.baseUrl;
  private sseStream: Subscription;
  message: Stock | undefined;
  allList: Stock[] = [];
  customList: Stock[] = [];
  data: Stock | undefined
  positive: Boolean = true

  constructor(private sseService: EventSourceService) {
    this.sseStream = this.sseService

    .observeMessages(`${this.BASE_URL}/stocks/ticker-price/stream`)
    .pipe(
      map((message: any) => {
        return message;
      }),
      take(10000)
    )
    .subscribe((message: Stock) => {
      this.allList.push(message);
      console.log(message.companyName + '  ' + message.openPrice)
      this.customList = this.allList.filter(stock => stock.stockName == this.stockInfo?.stockName) 
      this.data = this.customList.pop();
      if (this.data != undefined) {
        if (this.data.change >= 0) {
          this.positive = true
        } else {
          this.positive = false;
        }
      }
    });
  }


  ngOnInit(): void {
  }

  ngOnDestroy(): void {
    if (this.sseStream) {
      this.sseStream.unsubscribe();
    }
  }

}
