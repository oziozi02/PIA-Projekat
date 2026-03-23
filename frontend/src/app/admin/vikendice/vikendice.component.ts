import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { korisnik } from '../../models/korisnik';
import { DatePipe, NgFor, NgIf } from '@angular/common';
import { AdminService } from '../admin.service';
import { vikendicaPoslednje3ocene } from '../../models/responses/vikendicaPoslednje3ocene';

@Component({
  selector: 'app-vikendice',
  standalone: true,
  imports: [NgIf, NgFor, DatePipe],
  templateUrl: './vikendice.component.html',
  styleUrl: './vikendice.component.css'
})
export class VikendiceComponent implements OnInit {
  ngOnInit(): void {
    let adminData = localStorage.getItem("loggedAdmin");
    if(adminData){
      this.admin = JSON.parse(adminData);
    }
    this.adminServis.dohvatiSveVikendice().subscribe(data => {
      this.sveVikendice = data;
    });
  }

  ruter = inject(Router)

  admin = new korisnik()

  sveVikendice: vikendicaPoslednje3ocene[] = []

  adminServis = inject(AdminService)

  errorMessage = ""

  blokirajVikendicu(id: number, blokirana_do: string | null){
    if(blokirana_do){
      this.errorMessage = "Vikendica je već blokirana.";
      return;
    }
    this.adminServis.blokirajVikendicu(id).subscribe(data => {
      if(data.uspeh){
        this.errorMessage = "";
        this.adminServis.dohvatiSveVikendice().subscribe(data => {
          this.sveVikendice = data;
        });
      } else {
        this.errorMessage = data.poruka;
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
