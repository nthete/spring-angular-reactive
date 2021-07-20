import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Stock } from '../model/stock';

@Injectable({
  providedIn: 'root'
})
export class StocksService {

  constructor(private http: HttpClient) { }

  private readonly BASE_URL = environment.baseUrl;
  private readonly API = `${this.BASE_URL}/stocks`;
  private readonly isLocal = environment.isLocal;

  getAll(): Observable<Stock[]> {
    return this.http.get<Stock[]>(this.API);
  }
}
