import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { vikendica } from '../models/vikendica';
import { PocetnaService } from './pocetna.service';
import { VikendicaService } from '../turista/vikendice/vikendica.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-pocetna',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './pocetna.component.html',
  styleUrl: './pocetna.component.css'
})
export class PocetnaComponent implements OnInit {
  ngOnInit(): void {
    this.dohvatiPodatke()
  }

  ruter = inject(Router)

  pocetnaServis = inject(PocetnaService)
  vikendicaServis = inject(VikendicaService)

  ukupnoVikendica = 0
  ukupnoVlasnika = 0
  ukupnoTurista = 0
  brojRezervacija24h = 0
  brojRezervacija7d = 0
  brojRezervacija30d = 0

  vikendice: vikendica[] = []

  // Stanje sortiranja
  sortKolona: 'naziv' | 'mesto' | '' = ''
  sortSmer: 'asc' | 'desc' = 'asc'

  // Sortira vikendice prema trenutnom stanju sortiranja
  get sortedVikendice(): vikendica[] {
    if (!this.sortKolona) return this.vikendice;
    const arr = [...this.vikendice];
    const key = this.sortKolona === 'naziv' ? 'naziv' : 'mesto';
    arr.sort((a: any, b: any) => {
      const av = (a[key] || '').toString().toLowerCase();
      const bv = (b[key] || '').toString().toLowerCase();
      if (av < bv) return this.sortSmer === 'asc' ? -1 : 1;
      if (av > bv) return this.sortSmer === 'asc' ? 1 : -1;
      return 0;
    })
    return arr;
  }

  toggleSort(kolona: 'naziv' | 'mesto'){
    if(this.sortKolona === kolona){
      this.sortSmer = this.sortSmer === 'asc' ? 'desc' : 'asc'
    } else {
      this.sortKolona = kolona
      this.sortSmer = 'asc'
    }
  }

  dohvatiPodatke(){
    this.pocetnaServis.brojTurista().subscribe(data => {this.ukupnoTurista = data})
    this.pocetnaServis.brojVikendica().subscribe(data => {this.ukupnoVikendica = data})
    this.pocetnaServis.brojVlasnika().subscribe(data => {this.ukupnoVlasnika = data})
    this.pocetnaServis.brojVikendica24h().subscribe(data => {this.brojRezervacija24h = data})
    this.pocetnaServis.brojVikendica7d().subscribe(data => {this.brojRezervacija7d = data})
    this.pocetnaServis.brojVikendica30d().subscribe(data => {this.brojRezervacija30d = data})
    this.vikendicaServis.dohvatiSveVikendice().subscribe(data => {this.vikendice = data})
  }

  pretragaNaziv = ""
  pretragaMesto = ""

  pretrazi(){
    this.vikendicaServis.pretraziVikendice(this.pretragaNaziv, this.pretragaMesto).subscribe(data => {this.vikendice = data})
  }

  pocetna(){
    this.ruter.navigate([""])
  }

  login(){
    this.ruter.navigate(["login"])
  }

  registracija(){
    this.ruter.navigate(["registracija"])
  }

  promeniLozinku(){
    this.ruter.navigate(["promeniLozinku"])
  }
}
