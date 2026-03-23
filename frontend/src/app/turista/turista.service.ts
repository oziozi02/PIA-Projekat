import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { korisnik } from '../models/korisnik';
import { RezervacijaResponse } from '../models/responses/RezervacijaResponse';

@Injectable({
  providedIn: 'root'
})
export class TuristaService {

  constructor() { }

  url = "http://localhost:8080/turista"

  http = inject(HttpClient)

  dohvatiTuristu(korisnicko_ime: string){
    return this.http.post<korisnik>(`${this.url}/dohvatiTuristu`, korisnicko_ime)
  }

  azurirajTuristu(turista: korisnik, slika: File | null){
    const form = new FormData();
    // Append simple fields
    form.append('korisnicko_ime', turista.korisnicko_ime);
    form.append('ime', turista.ime);
    form.append('prezime', turista.prezime);
    form.append('adresa', turista.adresa);
    form.append('telefon', turista.telefon);
    form.append('email', turista.mejl);
    form.append('broj_kartice', turista.broj_kartice.toString());
    // Append file field if a file is selected
      if (slika) {
        form.append('slika', slika, slika.name);
      }
    return this.http.post<RezervacijaResponse>(`${this.url}/azurirajTuristu`, form);
  }
}
