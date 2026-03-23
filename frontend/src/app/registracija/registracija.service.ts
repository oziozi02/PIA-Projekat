import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { RegistracijaResponse } from '../models/responses/RegistracijaResponse';

@Injectable({
  providedIn: 'root'
})
export class RegistracijaService {

  constructor() { }

  url = "http://localhost:8080/registracija"

  http = inject(HttpClient)

  registrujSe(korisnicko_ime: string, lozinka: string, ime: string, prezime: string, pol: string, adresa: string, telefon: string, email: string, broj_kartice: string, uloga: string, slika: File | null){
    const form = new FormData();
    form.append('korisnicko_ime', korisnicko_ime);
    form.append('lozinka', lozinka);
    form.append('ime', ime);
    form.append('prezime', prezime);
    form.append('pol', pol);
    form.append('adresa', adresa);
    form.append('telefon', telefon);
    form.append('email', email);
    form.append('broj_kartice', broj_kartice);
    form.append('uloga', uloga);
      if (slika) {
        form.append('slika', slika, slika.name);
      }

    return this.http.post<RegistracijaResponse>(this.url, form);
  }
}
