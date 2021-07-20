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

  constructor(private sseService: EventSourceService) {
    this.sseStream = this.sseService
    .observeMessages(`${this.BASE_URL}/stocks/ticker-price/stream`)
    .pipe(
      map((message: any) => {
        return message;
      }),
      take(40)
    )
    .subscribe((message: Stock) => {
      this.message = message;
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
