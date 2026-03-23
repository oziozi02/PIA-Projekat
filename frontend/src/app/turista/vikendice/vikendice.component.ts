import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { VikendicaService } from './vikendica.service';
import { vikendica } from '../../models/vikendica';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-vikendice',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './vikendice.component.html',
  styleUrl: './vikendice.component.css'
})
export class VikendiceComponent implements OnInit {
  ngOnInit(): void {
    this.servis.dohvatiSveVikendice().subscribe(data => {this.sveVikendice = data})
  }

  ruter = inject(Router)

  servis = inject(VikendicaService)

  sveVikendice: vikendica[] = []

  pretragaNaziv = ""
  pretragaMesto = ""

  pretrazi(){
    this.servis.pretraziVikendice(this.pretragaNaziv, this.pretragaMesto).subscribe(data => {this.sveVikendice = data})
  }

  // Stanje sortiranja
  sortKolona: 'naziv' | 'mesto' | '' = ''
  sortSmer: 'asc' | 'desc' = 'asc'

  // Sortira vikendice prema trenutnom stanju sortiranja
  get sortedVikendice(): vikendica[] {
    if (!this.sortKolona) return this.sveVikendice;
    const arr = [...this.sveVikendice];
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

  // Izracunava prikaz zvezdica (vraca niz od 5 vrednosti: 'full' | 'half' | 'empty')
  starsFor(average: number) {
    const stars: ('full'|'half'|'empty')[] = [];
    if (!average || average <= 0) {
      return ['empty','empty','empty','empty','empty'];
    }
    let remaining = Math.round(average * 2) / 2; // zaokruzi na najblizi 0.5
    for (let i = 0; i < 5; i++) {
      if (remaining >= 1) { stars.push('full'); remaining -= 1; }
      else if (remaining === 0.5) { stars.push('half'); remaining -= 0.5; }
      else { stars.push('empty'); }
    }
    return stars;
  }

  toggleSort(kolona: 'naziv' | 'mesto'){
    if(this.sortKolona === kolona){
      this.sortSmer = this.sortSmer === 'asc' ? 'desc' : 'asc'
    } else {
      this.sortKolona = kolona
      this.sortSmer = 'asc'
    }
  }

  detalji(id: number){
    this.ruter.navigate([`turista/vikendice/${id}`])
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
