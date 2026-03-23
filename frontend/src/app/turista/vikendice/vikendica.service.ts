import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { vikendica } from '../../models/vikendica';
import { Cenovnik } from '../../models/cenovnik';
import { Komentar } from '../../models/Komentar';

@Injectable({
  providedIn: 'root'
})
export class VikendicaService {

  constructor() { }

  url = "http://localhost:8080/vikendica"

  http = inject(HttpClient)

  dohvatiSveVikendice(){
    return this.http.get<vikendica[]>(`${this.url}/dohvatiSveVikendice`)
  }

  pretraziVikendice(naziv: string, mesto: string){
    return this.http.get<vikendica[]>(`${this.url}/pretraziVikendice`, {params: {naziv, mesto}})
  }

  dohvatiVikendicuPoId(id: number){
    return this.http.post<vikendica>(`${this.url}/dohvatiVikendicuPoId`, id)
  }

  dohvatiSlikeVikendice(id: number){
    return this.http.post<string[]>(`${this.url}/dohvatiSlikeVikendice`, id)
  }

  dohvatiCenovnikVikendice(id: number){
    return this.http.post<Cenovnik[]>(`${this.url}/dohvatiCenovnikVikendice`, id)
  }

  dohvatiKomentareVikendice(id: number){
    return this.http.post<Komentar[]>(`${this.url}/dohvatiKomentareVikendice`, id)
  }
}
