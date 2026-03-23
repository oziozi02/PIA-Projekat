import { Component, inject, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { VikendicaService } from '../vikendica.service';
import { vikendica } from '../../../models/vikendica';
import { Cenovnik } from '../../../models/cenovnik';
import { Komentar } from '../../../models/Komentar';
import * as L from 'leaflet';
import 'leaflet/dist/leaflet.css';


@Component({
  selector: 'app-vikendica-detalji',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './vikendica-detalji.component.html',
  styleUrl: './vikendica-detalji.component.css'
})
export class VikendicaDetaljiComponent implements OnInit {
  ngOnInit(): void {
    const id = this.ruta.snapshot.paramMap.get('id');
    if(id){
      this.servis.dohvatiVikendicuPoId(Number(id)).subscribe(data => {
        if(data) {
          this.vikendica = data;
          this.usluge = data.usluge ? data.usluge.split(',').map(s => s.trim()) : [];
          this.servis.dohvatiSlikeVikendice(this.vikendica.id).subscribe(slikeData => {
            this.slike = slikeData
          });
          this.servis.dohvatiCenovnikVikendice(this.vikendica.id).subscribe(cenovnikData => {
            this.cenovnik = cenovnikData
          });
          this.servis.dohvatiKomentareVikendice(this.vikendica.id).subscribe(komentariData => {
            this.komentari = komentariData
            this.ukupnoStrana = Math.ceil(this.komentari.length / this.komentariPoStrani);
          });
          // inicijalizuj mapu sada kada je lokacija dostupna
          setTimeout(() => this.initMap(), 50);
        }
      });
    }
  }

  // Leaflet instance mape i markera
  private map: any = null;
  private marker: any = null;

  initMap() {
    // ukloni staru mapu ako postoji
    if (this.map) {
      try { this.map.remove(); } catch(e) {}
      this.map = null;
    }

    const lat = parseFloat(this.vikendica.lat as any) || 0;
    const lon = parseFloat(this.vikendica.lon as any) || 0;
    if (!lat || !lon) return;

    this.map = L.map('map').setView([lat, lon], 10);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 15,
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(this.map);

    this.marker = L.marker([lat, lon]).addTo(this.map);
    this.marker.bindPopup(`<strong>${this.vikendica.naziv}</strong><br/>${this.vikendica.mesto}`).openPopup();
  }

  iznajmi() {
    if(this.vikendica.blokirana_do) {
      alert("Vikendica je trenutno blokirana do " + new Date(this.vikendica.blokirana_do).toLocaleString());
      return;
    }
    this.ruter.navigate([`/turista/zakazivanje/${this.vikendica.id}`]);
  }


  ruta = inject(ActivatedRoute)

  ruter = inject(Router)

  servis = inject(VikendicaService)

  vikendica = new vikendica()

  cenovnik: Cenovnik[] = []
  usluge: string[] = []
  komentari: Komentar[] = []

  slike: string[] = []

  // Stanje galerije
  selectedImageIndex = 0

  //pagination komentara
  trenutnaStrana = 1;
  ukupnoStrana = 1;
  komentariPoStrani = 4;

  get prikazaniKomentari(): Komentar[] {
    const start = (this.trenutnaStrana - 1) * this.komentariPoStrani;
    return this.komentari.slice(start, start + this.komentariPoStrani);
  }

  promeniStranu(delta: number) {
    if(delta >= 1 && delta <= Math.ceil(this.komentari.length / this.komentariPoStrani)) {
      this.trenutnaStrana = delta;
    }
  }

  get selectedImage(): string | null {
    if (!this.slike || this.slike.length === 0) return null
    return this.slike[this.selectedImageIndex]
  }

  selectImage(i: number){
    if(i < 0 || i >= this.slike.length) return
    this.selectedImageIndex = i
  }

  prevImage(){
    if(!this.slike || this.slike.length === 0) return
    this.selectedImageIndex = (this.selectedImageIndex - 1 + this.slike.length) % this.slike.length
  }

  nextImage(){
    if(!this.slike || this.slike.length === 0) return
    this.selectedImageIndex = (this.selectedImageIndex + 1) % this.slike.length
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

  nazad(){
    this.ruter.navigate(['/turista/vikendice']);
  }
}
