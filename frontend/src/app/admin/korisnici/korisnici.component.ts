import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { korisnik } from '../../models/korisnik';
import { AdminService } from '../admin.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-korisnici',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './korisnici.component.html',
  styleUrl: './korisnici.component.css'
})
export class KorisniciComponent implements OnInit {
  ngOnInit(): void {
    let adminData = localStorage.getItem("loggedAdmin");
    if(adminData){
      this.admin = JSON.parse(adminData);
    }
    this.servis.dohvatiSveKorisnike().subscribe(data => {
      this.sviKorisnici = data;
    });
  }

  ruter = inject(Router)

  servis = inject(AdminService)

  sviKorisnici: korisnik[] = [];
  admin = new korisnik();
  errorMessage = ""

  azurirajKorisnika(korisnicko_ime: string){
    this.ruter.navigate([`admin/azuriraj-korisnika/${korisnicko_ime}`]);
  }

  blokirajKorisnika(korisnicko_ime: string){
    this.servis.promeniAktivacijuKorisnika(korisnicko_ime, false).subscribe(response => {
      if(response.uspeh){
        this.ngOnInit();
      }else{
        this.errorMessage = "Doslo je do greske.";
      }
    });
  }

  aktivirajKorisnika(korisnicko_ime: string){
    this.servis.promeniAktivacijuKorisnika(korisnicko_ime, true).subscribe(response => {
      if(response.uspeh){
        this.ngOnInit();
      }else{
        this.errorMessage = "Doslo je do greske.";
      }
    });
  }

  korisnici(){
    this.ruter.navigate(["admin/korisnici"]);
  }

  vikendice(){
    this.ruter.navigate(["admin/vikendice"]);
  }

  zahtevi(){
    this.ruter.navigate(["admin/zahtevi"]);
  }

  odjava(){
    localStorage.removeItem("loggedAdmin");
    this.ruter.navigate(["admin/login"]);
  }
}
