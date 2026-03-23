import { Component, inject, OnInit, ViewChildren, QueryList, ElementRef} from '@angular/core';
import { Router } from '@angular/router';
import { korisnik } from '../../models/korisnik';
import { KolonaPodaciVikendica } from '../../models/KolonaPodaci';
import { PitaPodaci } from '../../models/PitaPodaci';
import { StatistikaService } from './statistika.service';
import Chart from 'chart.js/auto';
import { NgForOf, NgIf } from '@angular/common';

@Component({
  selector: 'app-statistika',
  standalone: true,
  imports: [NgForOf, NgIf],
  templateUrl: './statistika.component.html',
  styleUrl: './statistika.component.css'
})
export class StatistikaComponent implements OnInit {
  ngOnInit(): void {
    let user = localStorage.getItem("loggedUser");
    if(user){
      this.vlasnik = JSON.parse(user);
      this.statistikaServis.dohvatiPodatkeZaKolone(this.vlasnik.korisnicko_ime).subscribe(data => {
        this.podaciZaKolone = data;
        setTimeout(() => this.createBarCharts(), 100);
      });
      this.statistikaServis.dohvatiPodatkeZaPitu(this.vlasnik.korisnicko_ime).subscribe(data => {
        this.podaciZaPite = data;
        setTimeout(() => this.createPieCharts(), 100);
      });
    }
  }

  ruter = inject(Router)

  statistikaServis = inject(StatistikaService)

  vlasnik = new korisnik()

  podaciZaKolone: KolonaPodaciVikendica[] = []
  podaciZaPite: PitaPodaci[] = []

  @ViewChildren('barChart') barCanvases!: QueryList<ElementRef<HTMLCanvasElement>>
  @ViewChildren('pieChart') pieCanvases!: QueryList<ElementRef<HTMLCanvasElement>>

 barCharts: Chart[] = []
 pieCharts: Chart[] = []

 createBarCharts() {
  // ukloni postojece bar chartove
  this.barCharts.forEach(c => c.destroy());
  this.barCharts = [];
    const canvases = this.barCanvases.toArray();
    for (let i = 0; i < this.podaciZaKolone.length; i++) {
      const kolona = this.podaciZaKolone[i];
      const canvasRef = canvases[i];
      if (!canvasRef) continue;

      const labels = kolona.kolonaPodatak.map(k => k.mesec);
      const data = kolona.kolonaPodatak.map(k => k.brojRezervacija);

      const ctx = canvasRef.nativeElement.getContext('2d');
      if (!ctx) continue;

      const chart = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: labels,
          datasets: [{
            label: 'Broj rezervacija',
            data: data,
            backgroundColor: 'rgba(54, 162, 235, 0.6)'
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: {
            x: { title: { display: true, text: 'Mesec' } },
            y: {
              beginAtZero: true,
              title: { display: true, text: 'Rezervacije' },
              ticks: {
                // samo celi brojevi
                stepSize: 1,
                precision: 0
              }
            }
          }
        }
      });

  this.barCharts.push(chart);
    }
  }

 createPieCharts() {
    this.pieCharts.forEach(c => c.destroy());
    this.pieCharts = [];

    const canvases = this.pieCanvases.toArray();
    for (let i = 0; i < this.podaciZaPite.length; i++) {
      const pita = this.podaciZaPite[i];
      const canvasRef = canvases[i];
      if (!canvasRef) continue;

      const ctx = canvasRef.nativeElement.getContext('2d');
      if (!ctx) continue;

      const values = [pita.brojRezervacijaVikendom, pita.brojRezervacijaRadnimDanom];
      const labels = ['Vikend', 'Radni dan'];

      const pie = new Chart(ctx, {
        type: 'pie',
        data: {
          labels: labels,
          datasets: [{ data: values, backgroundColor: ['rgba(75,192,192,0.7)', 'rgba(255,159,64,0.7)'] }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { position: 'bottom' } }
        }
      });

      this.pieCharts.push(pie);
    }
  }

  postojeRezervacije(): boolean {
    return this.podaciZaPite.some(pita => {
      return pita.brojRezervacijaRadnimDanom > 0 || pita.brojRezervacijaVikendom > 0;
    });
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
