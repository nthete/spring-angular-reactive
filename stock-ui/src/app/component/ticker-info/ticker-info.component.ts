import { Component, Input, OnChanges, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Stock } from 'src/app/model/stock';
import { EventSourceService } from 'src/app/service/event-source.service';
import { environment } from 'src/environments/environment';
import { map } from 'rxjs/operators';
import { SimpleChanges } from '@angular/core';


@Component({
  selector: 'app-ticker-info',
  templateUrl: './ticker-info.component.html',
  styleUrls: ['./ticker-info.component.scss']
})
export class TickerInfoComponent implements OnDestroy, OnChanges, OnInit {

  @Input() stockInfo: Stock | undefined;

  private readonly BASE_URL = environment.baseUrl;
  private sseStream: Subscription | undefined;
  message: Stock | undefined;
  positive: Boolean = true

  constructor(private sseService: EventSourceService) {
  }

  ngOnInit(): void {
    if (this.stockInfo && this.stockInfo!==undefined) {
      this.subscribeToTickerPrice(this.stockInfo.stockName)
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(!changes.stockInfo.isFirstChange() && this.stockInfo) {
      this.unsubscribe();
      this.subscribeToTickerPrice(this.stockInfo.stockName);
    }
  }

  ngOnDestroy(): void {
    if (this.sseStream) {
      this.sseStream.unsubscribe();
    }
  }

  private unsubscribe() {
    if (this.sseStream) {
      this.sseStream.unsubscribe();
    }
  }

  private subscribeToTickerPrice(stockName: String) {
    if (this.sseStream) {
      this.sseStream.unsubscribe();
    }
      this.sseStream = this.sseService
        .observeMessages(`${this.BASE_URL}/stocks/ticker-price/stream/` + stockName)
        .pipe(
          map((message: any) => {
            return message;
          })
        )
        .subscribe((message: Stock) => {
            this.message = message;
            if (this.message != undefined) {
              if (this.message.change >= 0) {
                this.positive = true
              } else {
                this.positive = false;
              }
            }
      });
    }
}
