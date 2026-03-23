import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { korisnik } from '../../models/korisnik';
import { ZahtevZaRegistraciju } from '../../models/zahteviZaRegistraciju';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../admin.service';

@Component({
  selector: 'app-zahtevi-za-registraciju',
  standalone: true,
  imports: [DatePipe, FormsModule, CommonModule],
  templateUrl: './zahtevi-za-registraciju.component.html',
  styleUrl: './zahtevi-za-registraciju.component.css'
})
export class ZahteviZaRegistracijuComponent implements OnInit {
  ngOnInit(): void {
    let adminData = localStorage.getItem("loggedAdmin");
    if(adminData){
      this.admin = JSON.parse(adminData);
    }
    this.servis.dohvatiZahteveZaRegistraciju().subscribe(data => {
      this.zahteviZaRegistraciju = data;
    });
  }

  ruter = inject(Router)

  servis = inject(AdminService)

  admin = new korisnik()
  zahteviZaRegistraciju: ZahtevZaRegistraciju[] = []
  errorMessage = ""
  odbijenId = -1
  komentarOdbijanja = ""

  prihvatiZahtev(id: number){
    this.servis.prihvatiZahtev(id).subscribe(success => {
      if(success){
        this.ngOnInit();
      } else {
        this.errorMessage = "Došlo je do greške prilikom prihvatanja zahteva.";
      }
    });
  }

  otvoriKomentar(id: number){
    if(this.odbijenId !== -1){
      this.odbijenId = -1;
      return;
    }
    this.odbijenId = id;
  }

  posaljiKomentar(id: number){
    this.servis.odbijZahtev(id, this.komentarOdbijanja).subscribe(success => {
      if(success){
        this.ngOnInit();
        this.odbijenId = -1;
        this.komentarOdbijanja = "";
      } else {
        this.errorMessage = "Došlo je do greške prilikom odbijanja zahteva.";
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
