import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

interface StreamData {
  data: string;
}

@Injectable({
  providedIn: 'root'
})
export class EventSourceService {

  private readonly BASE_URL = environment.baseUrl;

  constructor() { }

 observeMessages(url: string): Observable<StreamData> {
    return new Observable<StreamData>(obs => {
      const es = new EventSource(url);
      es.addEventListener('message', (evt: StreamData) => {
        obs.next(evt.data !=null ? JSON.parse(evt.data): evt.data);
      });
      return () => es.close();
    });
  }
}
