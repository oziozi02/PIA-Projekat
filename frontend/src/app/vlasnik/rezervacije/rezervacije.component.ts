import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TuristaService } from '../../turista/turista.service';
import { korisnik } from '../../models/korisnik';
import { AktivnaRezervacijaResponse } from '../../models/AktivnaRezervacijaResponse';
import { RezervacijeService } from './rezervacije.service';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from "@angular/forms";
import { FullCalendarModule } from '@fullcalendar/angular'; // for FullCalendar
import dayGridPlugin from '@fullcalendar/daygrid';
import { CalendarOptions } from '@fullcalendar/core/index.js';
import { srpskaLatinica } from '../../models/srpskaLatinica';

@Component({
  selector: 'app-rezervacije',
  standalone: true,
  imports: [DatePipe, FormsModule, CommonModule, FullCalendarModule],
  templateUrl: './rezervacije.component.html',
  styleUrl: './rezervacije.component.css'
})
export class RezervacijeComponent implements OnInit {
  ngOnInit(): void {
    let user = localStorage.getItem("loggedUser");
    if(user){
      let vlasnik = JSON.parse(user);
      this.turistaServis.dohvatiTuristu(vlasnik.korisnicko_ime).subscribe(data => {
        if(data){
          this.vlasnik = data;
          this.rezervacijeServis.dohvatiMojeRezervacije(this.vlasnik.korisnicko_ime).subscribe(rezData => {
            this.mojeRezervacije = rezData;
            this.neobradjeneRezervacije = rezData.filter(r => r.status === 'na_cekanju');
            this.calendarOptions.events = this.mojeRezervacije.map(r => ({
              id: r.id.toString(),
              title: r.naziv,
              start: r.datum_od + 'T' + r.vreme_od,
              end: r.datum_do + 'T' + r.vreme_do,
              color: r.status === 'na_cekanju' ? '#f3dd12ff' : '#62cc7bff',
              textColor: '#4b302aff',
              extendedProps: { status: r.status }
            }));
          });
        }
      });
    }
  }


  ruter = inject(Router)

  turistaServis = inject(TuristaService)
  rezervacijeServis = inject(RezervacijeService)

  vlasnik = new korisnik()
  mojeRezervacije: AktivnaRezervacijaResponse[] = []
  neobradjeneRezervacije: AktivnaRezervacijaResponse[] = []
  errorMessage = ""
  trenutniId = -1
  modalId = -1
  modalKomentarId = -1
  modalKomentar = ""
  showConfirmModal = false

  prihvatiRezervaciju(rezervacijaId: number){
    this.rezervacijeServis.prihvatiRezervaciju(rezervacijaId).subscribe(success => {
      if(success.uspeh){
        this.rezervacijeServis.dohvatiMojeRezervacije(this.vlasnik.korisnicko_ime).subscribe(rezData => {
            this.mojeRezervacije = rezData;
            this.neobradjeneRezervacije = rezData.filter(r => r.status === 'na_cekanju');
            this.trenutniId = -1;
            this.errorMessage = "";
            this.closeModal();
            this.calendarOptions.events = this.mojeRezervacije.map(r => ({
              id: r.id.toString(),
              title: r.naziv,
              start: r.datum_od + 'T' + r.vreme_od,
              end: r.datum_do + 'T' + r.vreme_do,
              color: r.status === 'na_cekanju' ? '#f3dd12ff' : '#62cc7bff',
              textColor: '#4b302aff',
              extendedProps: { status: r.status }
            }));
        });
      }
      else this.errorMessage = success.poruka;
    });
  }

  odbijUnesiKomentar(rezervacijaId: number){
    if(this.trenutniId != rezervacijaId) this.trenutniId = rezervacijaId
    else this.trenutniId = -1
  }

  odbijRezervaciju(rezervacijaId: number, komentar: string){
    if(komentar.trim().length == 0){
      this.errorMessage = "Morate uneti komentar prilikom odbijanja rezervacije.";
      return;
    }
    this.rezervacijeServis.odbijRezervaciju(rezervacijaId, komentar).subscribe(success => {
      if(success.uspeh){
        this.rezervacijeServis.dohvatiMojeRezervacije(this.vlasnik.korisnicko_ime).subscribe(rezData => {
            this.mojeRezervacije = rezData;
            this.neobradjeneRezervacije = rezData.filter(r => r.status === 'na_cekanju');
            this.trenutniId = -1;
            this.errorMessage = "";
            this.closeModal();
            this.calendarOptions.events = this.mojeRezervacije.map(r => ({
              id: r.id.toString(),
              title: r.naziv,
              start: r.datum_od + 'T' + r.vreme_od,
              end: r.datum_do + 'T' + r.vreme_do,
              color: r.status === 'na_cekanju' ? '#f3dd12ff' : '#62cc7bff',
              textColor: '#4b302aff',
              extendedProps: { status: r.status }
            }));
        });
      }
      else this.errorMessage = success.poruka;
    });
  }

  calendarOptions: CalendarOptions = {
    plugins: [dayGridPlugin],
    initialView: 'dayGridMonth',
    events: [],
    displayEventTime: false, // sakrij vremena ispred naslova
    height: 'auto',
    locale: srpskaLatinica,
    eventClick: this.klikNaDogadjaj.bind(this)
  };

  klikNaDogadjaj(eventInfo: any){
    if(eventInfo.event.extendedProps.status !== 'na_cekanju') return;
    const rezervacijaId = parseInt(eventInfo.event.id, 10);
    this.modalId = rezervacijaId;
    this.modalKomentar = "";
    this.showConfirmModal = true;
  }

  odbijUnesiKomentarZaModal(rezervacijaId: number){
    if(this.modalKomentarId != rezervacijaId) this.modalKomentarId = rezervacijaId
    else this.modalKomentarId = -1
  }

  closeModal(){
    this.showConfirmModal = false;
    this.modalId = -1;
    this.modalKomentarId = -1;
    this.modalKomentar = "";
  }

  profil(){
    this.ruter.navigate(["vlasnik/profil"])
  }

  rezervacije(){
    this.ruter.navigate(["vlasnik/rezervacije"])
  }

  mojeVikendice(){
    this.ruter.navigate(["vlasnik/moje-vikendice"])
  }

  statistika(){
    this.ruter.navigate(["vlasnik/statistika"])
  }

  odjava(){
    localStorage.removeItem("loggedUser")
    this.ruter.navigate(["login"])
  }
}
