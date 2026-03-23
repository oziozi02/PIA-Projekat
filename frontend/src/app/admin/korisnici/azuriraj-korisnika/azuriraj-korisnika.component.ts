import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AdminService } from '../../admin.service';
import { korisnik } from '../../../models/korisnik';
import { TuristaService } from '../../../turista/turista.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-azuriraj-korisnika',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './azuriraj-korisnika.component.html',
  styleUrl: './azuriraj-korisnika.component.css'
})
export class AzurirajKorisnikaComponent implements OnInit {
  ngOnInit(): void {
    let adminData = localStorage.getItem("loggedAdmin");
    if(adminData){
      this.admin = JSON.parse(adminData);
    }
    const korisnicko_ime = this.ruta.snapshot.paramMap.get('username');
    if(korisnicko_ime){
      this.korisnikServis.dohvatiTuristu(korisnicko_ime).subscribe(data => {
        if(data){
          this.korisnik = data;
          if(this.korisnik.slika_putanja != "default.jpg") this.profilna_slika = "http://localhost:8080/" + this.korisnik.slika_putanja;
          else this.profilna_slika = "default.jpg"
          this.pocetnaKartica();
        }
      });
    }
  }

  ruta = inject(ActivatedRoute)

  ruter = inject(Router)

  servis = inject(AdminService)
  korisnikServis = inject(TuristaService)

  korisnik = new korisnik();
  admin = new korisnik();
  profilna_slika = ""
  telefon = ""
  broj_kartice = ""
  ikonaKartice = ""
  karticaValidna = true
  errorMessage = ""
  selectedFile: File | null = null;

  onTelefonInput(event: Event) {
    const input = event.target as HTMLInputElement;
    let value = input.value;
    // + kao prvi karakter, ostalo brojevi
    if (value.startsWith('+')) {
      value = '+' + value.slice(1).replace(/[^\d]/g, '');
    } else {
      value = value.replace(/[^\d]/g, '');
    }
    input.value = value;
    this.korisnik.telefon = value;
  }

  onBrojKarticeInput(event: Event) {
    const input = event.target as HTMLInputElement;
    // Samo brojevi
    let value = input.value.replace(/[^\d]/g, '');
    input.value = value;
    this.broj_kartice = value;

    let valid = false;
    let msg = '';
    // Diners: pocinje sa 300, 301, 302, 303, 36, 38, duzina 15
    if ((/^300|301|302|303/.test(value) && value.length === 15) ||
        (/^36/.test(value) && value.length === 15) ||
        (/^38/.test(value) && value.length === 15)) {
      valid = true;
      this.ikonaKartice = "/Diners.png"
    }
    // MasterCard: pocinje sa 51-55, duzina 16
    else if (/^5[1-5]/.test(value) && value.length === 16) {
      valid = true;
      this.ikonaKartice = "/Mastercard.png"
    }
    // Visa: pocinje sa navedenim prefiksima, duzina 16
    else if ((/^4539|4556|4916|4532|4929|4485|4716/.test(value)) && value.length === 16) {
      valid = true;
      this.ikonaKartice = "/Visa.png"
    }
    else if (value.length > 0) {
      msg = 'Broj kartice nije validan za Diners, MasterCard ili Visa.';
    }
    this.karticaValidna = valid
    this.errorMessage = valid ? '' : msg;
  }

  pocetnaKartica(){
    let value = this.korisnik.broj_kartice.toString();
    if ((/^300|301|302|303/.test(value) && value.length === 15) ||
        (/^36/.test(value) && value.length === 15) ||
        (/^38/.test(value) && value.length === 15)) {
      this.ikonaKartice = "/Diners.png"
    }
    else if (/^5[1-5]/.test(value) && value.length === 16) {
      this.ikonaKartice = "/Mastercard.png"
    }
    else if ((/^4539|4556|4916|4532|4929|4485|4716/.test(value)) && value.length === 16) {
      this.ikonaKartice = "/Visa.png"
    }
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      const validTypes = ["image/jpeg", "image/png"];
      if (!validTypes.includes(file.type)) {
        this.errorMessage = "Slika mora biti u JPG ili PNG formatu.";
        return;
      }
      const img = new Image();
      const reader = new FileReader();
      reader.onload = (e: any) => {
        img.src = e.target.result;
        img.onload = () => {
          if (
            img.width < 100 || img.width > 300 ||
            img.height < 100 || img.height > 300
          ) {
            this.errorMessage = "Slika mora biti između 100x100 i 300x300 piksela.";
            return;
          }
          this.selectedFile = file;
          this.errorMessage = "";
        };
        img.onerror = () => {
          this.errorMessage = "Greška pri učitavanju slike.";
        };
      };
      this.profilna_slika = URL.createObjectURL(file);
      reader.readAsDataURL(file);
    }
  }

  azurirajProfil(){
    this.servis.azurirajKorisnika(this.korisnik, this.selectedFile).subscribe(data => {
      if(data.uspeh){
        this.korisnikServis.dohvatiTuristu(this.korisnik.korisnicko_ime).subscribe(data => {
        if(data){
          this.korisnik = data;
          if(this.korisnik.slika_putanja != "default.jpg") this.profilna_slika = `http://localhost:8080/${this.korisnik.slika_putanja}?t=${Date.now()}`
          else this.profilna_slika = "default.jpg"
          this.pocetnaKartica();
          this.errorMessage = "Uspešno ažuriran korisnik.";
        }
      });
      } else {
        this.errorMessage = data.poruka;
      }
    });
  }
  nazad(){
    this.ruter.navigate(["admin/korisnici"]);
  }
}
