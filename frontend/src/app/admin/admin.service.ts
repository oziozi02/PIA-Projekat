import { HttpClient } from '@angular/common/http';
import { inject, Injectable, OnInit } from '@angular/core';
import { korisnik } from '../models/korisnik';
import { RezervacijaResponse } from '../models/responses/RezervacijaResponse';
import { ZahtevZaRegistraciju } from '../models/zahteviZaRegistraciju';
import { vikendicaPoslednje3ocene } from '../models/responses/vikendicaPoslednje3ocene';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor() { }

  url = "http://localhost:8080/admin"

  http = inject(HttpClient)

  login(username: string, password: string) {
    return this.http.post<korisnik>("http://localhost:8080/login/admin", { korisnicko_ime: username, lozinka_hash: password });
  }

  dohvatiSveKorisnike() {
    return this.http.get<korisnik[]>(`${this.url}/dohvatiSveKorisnike`);
  }

  promeniAktivacijuKorisnika(korisnicko_ime: string, aktivan: boolean) {
    return this.http.get<RezervacijaResponse>(`${this.url}/promeniAktivacijuKorisnika`, { params: { korisnicko_ime: korisnicko_ime, aktivan: aktivan.toString() } });
  }

  azurirajKorisnika(korisnik: korisnik, slika: File | null) {
    const formData = new FormData();
    formData.append('korisnicko_ime', korisnik.korisnicko_ime);
    formData.append('ime', korisnik.ime);
    formData.append('prezime', korisnik.prezime);
    formData.append('adresa', korisnik.adresa);
    formData.append('telefon', korisnik.telefon);
    formData.append('email', korisnik.mejl);
    formData.append('broj_kartice', korisnik.broj_kartice.toString());
    formData.append('uloga', korisnik.uloga);
    formData.append('aktivan', korisnik.aktivan.toString());
    if (slika) {
      formData.append('slika', slika);
    }
    return this.http.post<RezervacijaResponse>(`${this.url}/azurirajKorisnika`, formData);
  }

  dohvatiZahteveZaRegistraciju() {
    return this.http.get<ZahtevZaRegistraciju[]>(`${this.url}/dohvatiZahteveZaRegistraciju`);
  }

  prihvatiZahtev(id: number) {
    return this.http.get<boolean>(`${this.url}/prihvatiZahtev`, { params: { id: id } });
  }

  odbijZahtev(id: number, komentar: string) {
    return this.http.get<boolean>(`${this.url}/odbijZahtev`, { params: { id: id, komentar: komentar } });
  }

  dohvatiSveVikendice() {
    return this.http.get<vikendicaPoslednje3ocene[]>(`${this.url}/dohvatiSveVikendice`);
  }

  blokirajVikendicu(id: number) {
    return this.http.get<RezervacijaResponse>(`${this.url}/blokirajVikendicu`, { params: { id: id } });
  }
}
