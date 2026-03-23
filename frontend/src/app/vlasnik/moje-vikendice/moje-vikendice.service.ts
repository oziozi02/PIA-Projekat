import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { vikendica } from '../../models/vikendica';
import { RezervacijaResponse } from '../../models/responses/RezervacijaResponse';
import { NovaVikendicaRequest } from '../../models/requests/NovaVikendicaRequest';

@Injectable({
  providedIn: 'root'
})
export class MojeVikendiceService {

  constructor() { }

  url = "http://localhost:8080/vikendica"

  http = inject(HttpClient)

  dohvatiMojeVikendice(korisnicko_ime: string){
    return this.http.get<vikendica[]>(`${this.url}/dohvatiMojeVikendice`, {params: {korisnicko_ime}})
  }

  obrisiVikendicu(vikendicaId: number){
    return this.http.get<RezervacijaResponse>(`${this.url}/obrisiVikendicu`, {params: {vikendica_id: vikendicaId}})
  }

  dodajVikendicu(novaVikendica: NovaVikendicaRequest){
    return this.http.post<RezervacijaResponse>(`${this.url}/dodajVikendicu`, novaVikendica)
  }

  azurirajVikendicu(izmenjenaVikendica: NovaVikendicaRequest, obrisaneSlike: string[]){
    const request = {
      vikendica: izmenjenaVikendica,
      obrisane_slike: obrisaneSlike
    }
    return this.http.post<RezervacijaResponse>(`${this.url}/azurirajVikendicu`, request);
  }
}
