import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { korisnik } from '../models/korisnik';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor() { }

  url = "http://localhost:8080/login"

  http = inject(HttpClient)

  login(username: string, password: string){
    const body = {korisnicko_ime:username, lozinka_hash: password}
    return this.http.post<korisnik>(`${this.url}`, body)
  }

}
