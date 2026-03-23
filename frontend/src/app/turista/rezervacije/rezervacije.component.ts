import { Component, inject, OnInit } from '@angular/core';
import { TuristaService } from '../turista.service';
import { VikendicaService } from '../vikendice/vikendica.service';
import { Router } from '@angular/router';
import { DatePipe, NgIf, NgFor } from '@angular/common';
import { AktivnaRezervacijaResponse } from '../../models/AktivnaRezervacijaResponse';
import { RezervacijeService } from './rezervacije.service';
import { korisnik } from '../../models/korisnik';
import { ArhiviranaRezervacijaResponse } from '../../models/responses/arhiviranaRezervacijaResponse';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-rezervacije',
  standalone: true,
  imports: [DatePipe, NgIf, NgFor, FormsModule],
  templateUrl: './rezervacije.component.html',
  styleUrl: './rezervacije.component.css'
})
export class RezervacijeComponent implements OnInit {
  ngOnInit(): void {
    let user = localStorage.getItem("loggedUser");
    if(user){
      let turista = JSON.parse(user);
      this.turistaServis.dohvatiTuristu(turista.korisnicko_ime).subscribe(data => {
        if(data){
          this.turista = data;
          this.rezervacijeServis.dohvatiAktivneRezervacije(data.korisnicko_ime).subscribe(rezData => {
            this.aktivneRezervacije = rezData;
          });
          this.rezervacijeServis.dohvatiArhiviraneRezervacije(data.korisnicko_ime).subscribe(arhData => {
            this.arhiviraneRezervacije = arhData;
          });
        }
      });
    }
  }

  ruter = inject(Router)

  turistaServis = inject(TuristaService)
  vikendiceServis = inject(VikendicaService)
  rezervacijeServis = inject(RezervacijeService)

  aktivneRezervacije: AktivnaRezervacijaResponse[] = []
  arhiviraneRezervacije: ArhiviranaRezervacijaResponse[] = []
  turista = new korisnik()
  errorMessage1 = ""
  errorMessage2 = ""
  trenutniId = -1
  komentarTekst = ""
  ocena = 0

  otkaziRezervaciju(id: number){
    this.rezervacijeServis.otkaziRezervaciju(id).subscribe((response) => {
      if(response.uspeh){
        this.rezervacijeServis.dohvatiAktivneRezervacije(this.turista.korisnicko_ime).subscribe(rezData => {
          this.aktivneRezervacije = rezData;
        });
      } else {
        this.errorMessage1 = response.poruka;
      }
    });
  }

  nemoguceOtkazivanje(rezervacija: AktivnaRezervacijaResponse): boolean {
    let danas = new Date();
    let datumDolaska = new Date(`${rezervacija.datum_od}T${rezervacija.vreme_od}`);
    let razlikaUMilisekundama = datumDolaska.getTime() - danas.getTime();
    let razlikaUDanima = razlikaUMilisekundama / (1000 * 3600 * 24);
    return razlikaUDanima < 1;
  }

  nemogucKomentar(komentar: string, ocena: number, status: string): boolean {
    return status !== "odobrena" || (komentar.trim().length > 0 && ocena > 0);
  }

  brojZvezda(ocena: number): number[] {
    return Array(ocena).fill(0);
  }

  otvoriKomponentu(id: number) {
    if(this.trenutniId === id) this.trenutniId = -1;
    else this.trenutniId = id;
    this.komentarTekst = "";
    this.ocena = 0;
  }

  ostaviKomentar(rezervacijaId: number) {
    if(this.komentarTekst.trim().length == 0 || this.ocena <= 0){
      this.errorMessage2 = "Morate uneti komentar i ocenu pre slanja.";
      return;
    }
    this.rezervacijeServis.ostaviKomentar(rezervacijaId, this.komentarTekst, this.ocena).subscribe((response) => {
      if(response.uspeh){
        this.rezervacijeServis.dohvatiArhiviraneRezervacije(this.turista.korisnicko_ime).subscribe(arhData => {
          this.arhiviraneRezervacije = arhData;
          this.trenutniId = -1;
          this.komentarTekst = "";
          this.ocena = 0;
          this.errorMessage2 = "";
        });
      } else {
        this.errorMessage2 = response.poruka;
      }
    });
  }

  profil(){
    this.ruter.navigate(["turista/profil"])
  }

  vikendice(){
    this.ruter.navigate(["turista/vikendice"])
  }

  rezervacije(){
    this.ruter.navigate(["turista/rezervacije"])
  }

  odjava(){
    localStorage.removeItem("loggedUser")
    this.ruter.navigate(["login"])
  }
}
