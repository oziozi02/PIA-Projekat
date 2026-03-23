import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { AktivnaRezervacijaResponse } from '../../models/AktivnaRezervacijaResponse';
import { RezervacijaResponse } from '../../models/responses/RezervacijaResponse';
import { ArhiviranaRezervacijaResponse } from '../../models/responses/arhiviranaRezervacijaResponse';

@Injectable({
  providedIn: 'root'
})
export class RezervacijeService {

  constructor() { }

  url = "http://localhost:8080/rezervacije"

  http = inject(HttpClient)

  dohvatiAktivneRezervacije(korisnickoIme: string) {
    return this.http.get<AktivnaRezervacijaResponse[]>(`${this.url}/dohvatiAktivneRezervacije`, {params: {turista: korisnickoIme}});
  }

  otkaziRezervaciju(id: number) {
    return this.http.post<RezervacijaResponse>(`${this.url}/otkaziRezervaciju`, id);
  }

  dohvatiArhiviraneRezervacije(korisnickoIme: string) {
    return this.http.get<ArhiviranaRezervacijaResponse[]>(`${this.url}/dohvatiArhiviraneRezervacije`, {params: {turista: korisnickoIme}});
  }

  ostaviKomentar(id: number, tekst: string, ocena: number) {
    return this.http.get<RezervacijaResponse>(`${this.url}/ostaviKomentar`, {params: {id, tekst, ocena}});
  }
}
