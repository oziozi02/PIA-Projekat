import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PocetnaService {

  constructor() { }

  url = "http://localhost:8080/pocetna"

  http = inject(HttpClient)

  brojVlasnika(){
    return this.http.get<number>(`${this.url}/brojVlasnika`)
  }

  brojVikendica(){
    return this.http.get<number>(`${this.url}/brojVikendica`)
  }

  brojTurista(){
    return this.http.get<number>(`${this.url}/brojTurista`)
  }

  brojVikendica24h(){
    return this.http.get<number>(`${this.url}/brojVikendica24h`)
  }

  brojVikendica7d(){
    return this.http.get<number>(`${this.url}/brojVikendica7d`)
  }

  brojVikendica30d(){
    return this.http.get<number>(`${this.url}/brojVikendica30d`)
  }

  
}
