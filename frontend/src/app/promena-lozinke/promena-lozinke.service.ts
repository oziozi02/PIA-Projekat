import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { PromenaLozinkeResponse } from '../models/responses/PromenaLozinkeResponse';

@Injectable({
  providedIn: 'root'
})
export class PromenaLozinkeService {

  constructor() { }

  url = "http://localhost:8080/promenaLozinke"

  http = inject(HttpClient)

  promeniLozinku(korisnicko_ime: string, stara_lozinka: string, nova_lozinka: string){
    const body = {korisnicko_ime: korisnicko_ime, staraLozinka: stara_lozinka, novaLozinka: nova_lozinka}
    return this.http.post<PromenaLozinkeResponse>(`${this.url}`, body)
  }
}
