import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { KolonaPodaciVikendica } from '../../models/KolonaPodaci';
import { PitaPodaci } from '../../models/PitaPodaci';

@Injectable({
  providedIn: 'root'
})
export class StatistikaService {

  constructor() { }

  url = "http://localhost:8080/statistika"

  http = inject(HttpClient)

  dohvatiPodatkeZaKolone(korisnicko_ime: string){
    return this.http.get<KolonaPodaciVikendica[]>(`${this.url}/dohvatiPodatkeZaKolone`, {params: {korisnicko_ime: korisnicko_ime}})
  }

  dohvatiPodatkeZaPitu(korisnicko_ime: string){
    return this.http.get<PitaPodaci[]>(`${this.url}/dohvatiPodatkeZaPitu`, {params: {korisnicko_ime: korisnicko_ime}})
  }
}
