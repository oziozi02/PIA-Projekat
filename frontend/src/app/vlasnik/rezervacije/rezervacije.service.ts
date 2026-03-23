import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { AktivnaRezervacijaResponse } from '../../models/AktivnaRezervacijaResponse';
import { RezervacijaResponse } from '../../models/responses/RezervacijaResponse';

@Injectable({
  providedIn: 'root'
})
export class RezervacijeService {

  constructor() { }

  url = "http://localhost:8080/rezervacije"

  http = inject(HttpClient)

  dohvatiMojeRezervacije(korisnicko_ime: string){
    return this.http.get<AktivnaRezervacijaResponse[]>(`${this.url}/dohvatiMojeRezervacije`, {params: {korisnicko_ime}})
  }

  prihvatiRezervaciju(rezervacijaId: number){
    return this.http.post<RezervacijaResponse>(`${this.url}/prihvatiRezervaciju`, rezervacijaId)
  }

  odbijRezervaciju(rezervacijaId: number, komentar: string){
    return this.http.get<RezervacijaResponse>(`${this.url}/odbijRezervaciju`, {params: {rezervacijaId: rezervacijaId, komentar: komentar}})
  }
}
