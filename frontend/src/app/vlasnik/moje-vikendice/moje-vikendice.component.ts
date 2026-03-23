import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { vikendica } from '../../models/vikendica';
import { korisnik } from '../../models/korisnik';
import { CommonModule, DatePipe } from '@angular/common';
import { MojeVikendiceService } from './moje-vikendice.service';
import { TuristaService } from '../../turista/turista.service';
import { FormsModule } from '@angular/forms';
import { Cenovnik } from '../../models/cenovnik';
import { NovaVikendicaRequest } from '../../models/requests/NovaVikendicaRequest';


@Component({
  selector: 'app-moje-vikendice',
  standalone: true,
  imports: [DatePipe, CommonModule, FormsModule],
  templateUrl: './moje-vikendice.component.html',
  styleUrl: './moje-vikendice.component.css'
})
export class MojeVikendiceComponent implements OnInit {
  ngOnInit(): void {
    let user = localStorage.getItem("loggedUser");
    if(user){
      let vlasnik = JSON.parse(user);
      this.vlasnikServis.dohvatiTuristu(vlasnik.korisnicko_ime).subscribe(data => {
        if(data){
          this.vlasnik = data;
          this.vikendiceServis.dohvatiMojeVikendice(this.vlasnik.korisnicko_ime).subscribe(vikData => {
            this.vikendice = vikData;
          });
        }
      });
    }
  }


  ruter = inject(Router)

  vlasnik = new korisnik()

  vikendice: vikendica[] = []

  cenovnikLeto = 0;
  cenovnikZima = 0;

  novaVikendica: vikendica = new vikendica();

  vikendiceServis = inject(MojeVikendiceService)
  vlasnikServis = inject(TuristaService)

  errorMessage = ""
  errorMessage2 = ""

  izmeniVikendicu(vikendicaId: number){
    this.ruter.navigate([`vlasnik/azuriraj-vikendicu/${vikendicaId}`]);
  }

  // modal za potvrdu brisanja
  selectedToDeleteId: number | null = null;
  showConfirmModal = false;

  obrisiVikendicu(vikendicaId: number){
    // prikazi modal
    this.selectedToDeleteId = vikendicaId;
    this.showConfirmModal = true;
  }

  cancelDelete(){
    this.selectedToDeleteId = null;
    this.showConfirmModal = false;
  }

  confirmDelete(){
    if(this.selectedToDeleteId == null) return;
    const id = this.selectedToDeleteId;
    this.vikendiceServis.obrisiVikendicu(id).subscribe(success => {
      if(success.uspeh){
        this.vikendiceServis.dohvatiMojeVikendice(this.vlasnik.korisnicko_ime).subscribe(vikData => {
          this.vikendice = vikData;
          this.errorMessage2 = success.poruka;
        });
      }
      else this.errorMessage = success.poruka;
      this.cancelDelete();
    });
  }

  onJsonSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      const reader = new FileReader();
      reader.onload = (e) => {
        try {
          const json = JSON.parse(e.target?.result as string);
          // Učitavanje podataka iz JSON-a u novu vikendicu
          this.novaVikendica.naziv = typeof json.naziv === 'string' ? json.naziv : "";
          this.novaVikendica.mesto = typeof json.mesto === 'string' ? json.mesto : "";
          this.novaVikendica.telefon = typeof json.telefon === 'string' ? json.telefon : "";
          this.novaVikendica.usluge = typeof json.usluge === 'string' ? json.usluge :
          Array.isArray(json.usluge) ? json.usluge.join(", ") : "";
          this.novaVikendica.lat = Number(json.lat) || 0;
          this.novaVikendica.lon = Number(json.lon) || 0;
          this.cenovnikLeto = Number(json.cenovnikLeto) || 0;
          this.cenovnikZima = Number(json.cenovnikZima) || 0;
          this.errorMessage2 = "JSON fajl uspešno učitan.";
        } catch (error) {
          this.errorMessage2 = "Greška prilikom učitavanja JSON fajla.";
        }
      };
      reader.readAsText(file);
    }
  }

  slikeZaPrikaz: string[] = [];

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      const file = input.files[0];
      const validTypes = ["image/jpeg", "image/png"];
      if (!validTypes.includes(file.type)) {
        this.errorMessage = "Slika mora biti u JPG ili PNG formatu.";
        return;
      }
      const files = Array.from(input.files);
      files.forEach(file => {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.slikeZaPrikaz.push(e.target?.result as string);
        };
        reader.readAsDataURL(file);
      });
      this.errorMessage = "";
    }
  }

  obrisiSliku(slika: string){
    this.slikeZaPrikaz = this.slikeZaPrikaz.filter(s => s !== slika);
  }

  onTelefonInput(event: Event) {
    const input = event.target as HTMLInputElement;
    let value = input.value;
    // + kao privi karakter, ostalo moraju biti cifre
    if (value.startsWith('+')) {
      value = '+' + value.slice(1).replace(/[^\d]/g, '');
    } else {
      value = value.replace(/[^\d]/g, '');
    }
    input.value = value;
    this.novaVikendica.telefon = value;
  }

  dodajVikendicu(){
    if(!this.novaVikendica.naziv || !this.novaVikendica.mesto || !this.novaVikendica.telefon || !this.slikeZaPrikaz.length || this.cenovnikLeto <= 0 || this.cenovnikZima <= 0 || this.novaVikendica.lon == 0 || this.novaVikendica.lat == 0 || !this.novaVikendica.usluge) {
      this.errorMessage2 = "Sva polja su obavezna.";
      return;
    }
    let vikRequest = new NovaVikendicaRequest();
    this.novaVikendica.vlasnik = this.vlasnik.korisnicko_ime;
    vikRequest.vikendica = this.novaVikendica;
    vikRequest.slike = this.slikeZaPrikaz;
    vikRequest.cenovnikLeto = this.cenovnikLeto;
    vikRequest.cenovnikZima = this.cenovnikZima;
    this.vikendiceServis.dodajVikendicu(vikRequest).subscribe(success => {
      if(success.uspeh){
        this.vikendiceServis.dohvatiMojeVikendice(this.vlasnik.korisnicko_ime).subscribe(vikData => {
            this.vikendice = vikData;
        });
        this.novaVikendica = new vikendica();
        this.slikeZaPrikaz = [];
        this.errorMessage2 = success.poruka;
        this.cenovnikLeto = 0;
        this.cenovnikZima = 0;
      }
      else this.errorMessage2 = success.poruka;
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
