import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { RezervacijaResponse } from '../../models/responses/RezervacijaResponse';

@Injectable({
  providedIn: 'root'
})
export class ZakazivanjeService {

  constructor() { }

  url = "http://localhost:8080/zakazivanje"

  http = inject(HttpClient)

  izracunajCenuZakazivanja(vikendicaId: number, datumOd: string, datumDo: string, brojOdraslih: number, brojDece: number) {
    return this.http.get<number>(`${this.url}/izracunajCenuZakazivanja`, {params: {
      vikendica_id: vikendicaId,
      datumOd: datumOd,
      datumDo: datumDo,
      brojOdraslih: brojOdraslih,
      brojDece: brojDece
    }});
  }

  potvrdiZakazivanje(vikendicaId: number, korisnickoIme: string, datumOd: string, datumDo: string, vremeOd: string, vremeDo: string,
     brojOdraslih: number, brojDece: number, brojKartice: string, dodatniZahtevi: string) {
    const body = {
      vikendica_id: vikendicaId,
      korisnicko_ime: korisnickoIme,
      datum_od: datumOd,
      datum_do: datumDo,
      vreme_od: vremeOd,
      vreme_do: vremeDo,
      broj_odraslih: brojOdraslih,
      broj_dece: brojDece,
      broj_kartice: brojKartice,
      dodatni_zahtevi: dodatniZahtevi
    }
    return this.http.post<RezervacijaResponse>(`${this.url}/potvrdiZakazivanje`, body);
  }
}
