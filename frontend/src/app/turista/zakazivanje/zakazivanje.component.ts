import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { korisnik } from '../../models/korisnik';
import { vikendica } from '../../models/vikendica';
import { VikendicaService } from '../vikendice/vikendica.service';
import { TuristaService } from '../turista.service';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ZakazivanjeService } from './zakazivanje.service';

@Component({
  selector: 'app-zakazivanje',
  standalone: true,
  imports: [CommonModule, FormsModule, DatePipe],
  templateUrl: './zakazivanje.component.html',
  styleUrl: './zakazivanje.component.css'
})
export class ZakazivanjeComponent implements OnInit {
  ngOnInit(): void {
    const id = this.ruta.snapshot.paramMap.get('id');
    if(id){
      this.vikendicaServis.dohvatiVikendicuPoId(Number(id)).subscribe(data => {
        if(data) {
          this.vikendica = data;

        }
      });
    }
    let user = localStorage.getItem("loggedUser");
    if(user){
      this.turista = JSON.parse(user);
      this.turistaServis.dohvatiTuristu(this.turista.korisnicko_ime).subscribe(data => {
        if(data){
          this.turista = data;
          this.brojKartice = this.turista.broj_kartice.toString();
        }
      });
    }
    this.dostupnaVremenaDolaska = this.pripremiSate(14,22);
    this.dostupnaVremenaOdlaska = this.pripremiSate(6,10);
    // izracunaj min datum kao sutra
    const t = new Date();
    t.setDate(t.getDate() + 1);
    const yyyy = t.getFullYear();
    const mm = String(t.getMonth() + 1).padStart(2, '0');
    const dd = String(t.getDate()).padStart(2, '0');
    this.minDate = `${yyyy}-${mm}-${dd}`;
  }

  ruta = inject(ActivatedRoute)

  ruter = inject(Router)

  vikendicaServis = inject(VikendicaService)
  turistaServis = inject(TuristaService)
  zakazivanjeServis = inject(ZakazivanjeService)

  turista = new korisnik()

  vikendica = new vikendica()

  korak = 1;
  datumOd = "";
  datumDo = "";
  vremeOd = "14:00";
  vremeDo = "10:00";
  brojOdraslih = 1;
  brojDece = 0;
  brojKartice = "";
  cena = 0;
  dodatniZahtevi = "";
  dostupnaVremenaDolaska: string[] = [];
  dostupnaVremenaOdlaska: string[] = [];
  errorMessage = "";
  minDate: string = '';
  karticaValidna = true;

  pripremiSate(pocetak: number, kraj: number): string[] {
    let sati: string[] = [];
    for(let i = pocetak; i <= kraj; i++){
      let sat = i < 10 ? `0${i}:00` : `${i}:00`;
      sati.push(sat);
    }
    return sati;
  }

  nastavi(){
    if(this.datumOd == "" || this.datumDo == "" || this.vremeOd == "" || this.vremeDo == ""){
      this.errorMessage = "Sva polja moraju biti popunjena.";
      return;
    }
    if(new Date(this.datumOd) >= new Date(this.datumDo)){
      this.errorMessage = "Datum odlaska mora biti posle datuma dolaska.";
      return;
    }
    if(this.korak == 1){
      this.korak = 2;
      this.errorMessage = "";
      this.zakazivanjeServis.izracunajCenuZakazivanja(this.vikendica.id, this.datumOd, this.datumDo, this.brojOdraslih, this.brojDece).subscribe(data => {
        if(data){
          this.cena = data;
        }
      });
    }
  }

  onBrojKarticeInput(event: Event) {
    const input = event.target as HTMLInputElement;
    // Dozvoli samo cifre
    let value = input.value.replace(/[^\d]/g, '');
    input.value = value;
    this.brojKartice = value;

    let valid = false;
    let msg = '';
    // Diners: pocinje sa 300, 301, 302, 303, 36, 38, duzina 15
    if ((/^300|301|302|303/.test(value) && value.length === 15) ||
        (/^36/.test(value) && value.length === 15) ||
        (/^38/.test(value) && value.length === 15)) {
      valid = true;
      this.brojKartice = "/Diners.png"
    }
    // MasterCard: pocinje sa 51-55, duzina 16
    else if (/^5[1-5]/.test(value) && value.length === 16) {
      valid = true;
    }
    // Visa: pocinje sa navedenim prefiksima, duzina 16
    else if ((/^4539|4556|4916|4532|4929|4485|4716/.test(value)) && value.length === 16) {
      valid = true;
    }
    else if (value.length > 0) {
      msg = 'Broj kartice nije validan za Diners, MasterCard ili Visa.';
    }
    this.karticaValidna = valid
    this.errorMessage = valid ? '' : msg;
  }

  nazad(){
    if(this.korak == 2) this.korak = 1;
  }

  odustani(){
    this.ruter.navigate([`/turista/vikendice/${this.vikendica.id}`]);
  }

  zakazi(){
    if(!this.karticaValidna){
      return;
    }
    this.zakazivanjeServis.potvrdiZakazivanje(this.vikendica.id, this.turista.korisnicko_ime, this.datumOd, this.datumDo, this.vremeOd, this.vremeDo,
      this.brojOdraslih, this.brojDece, this.brojKartice, this.dodatniZahtevi).subscribe(data => {
        if(data.uspeh){
          alert(data.poruka);
          this.ruter.navigate(['/turista/vikendice']);
        } else {
          this.errorMessage = data.poruka;
        }
      });
  }
}
