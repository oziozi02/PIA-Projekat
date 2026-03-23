  import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { RegistracijaService } from './registracija.service';

@Component({
  selector: 'app-registracija',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './registracija.component.html',
  styleUrl: './registracija.component.css'
})
export class RegistracijaComponent {


  ruter = inject(Router)

  servis = inject(RegistracijaService)

  korisnicko_ime = ""
  lozinka = ""
  ime = ""
  prezime = ""
  pol = ""
  adresa = ""
  telefon = ""
  mejl = ""
  broj_kartice = ""
  uloga = ""
  errorMessage = ""
  passwordErrorMessage = ""
  selectedFile: File | null = null;
  ikonaKartice = ""
  karticaValidna = false
  lozinkaValidna = false

  proveraLozinke(password: string) {
    const pw = password;
    this.lozinkaValidna = false
    if(pw.length < 6 || pw.length > 10) {
      this.passwordErrorMessage = "Lozinka mora imati između 6 i 10 karaktera.";
      return;
    }
    if(!/^[A-Za-z]/.test(pw)) {
      this.passwordErrorMessage = "Lozinka mora početi slovom.";
      return;
    }
    if(!/[A-Z]/.test(pw)) {
      this.passwordErrorMessage = "Lozinka mora imati bar jedno veliko slovo.";
      return;
    }
    if((pw.match(/[a-z]/g) || []).length < 3) {
      this.passwordErrorMessage = "Lozinka mora imati bar tri mala slova.";
      return;
    }
    if(!/[0-9]/.test(pw)) {
      this.passwordErrorMessage = "Lozinka mora imati bar jedan broj.";
      return;
    }
    if(!/[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]/.test(pw)) {
      this.passwordErrorMessage = "Lozinka mora imati bar jedan specijalni karakter.";
      return;
    }
    this.lozinkaValidna = true
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
      reader.readAsDataURL(file);
    }
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
    this.telefon = value;
  }

  onBrojKarticeInput(event: Event) {
    const input = event.target as HTMLInputElement;
    // Samo dozvoli brojeve
    let value = input.value.replace(/[^\d]/g, '');
    input.value = value;
    this.broj_kartice = value;

    // Provera kartice
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


  registrujSe(){
    if(this.korisnicko_ime == "" || this.lozinka == "" || this.ime == "" || this.prezime == "" || this.pol == "" || this.adresa == "" || this.telefon == "" || this.mejl == "" || this.broj_kartice == "" || this.uloga == ""){
      this.errorMessage = "Unesite sve podatke"
      return
    }
    if(!this.lozinkaValidna){
      this.errorMessage = "Lozinka nije validna"
      return
    }
    if(!this.karticaValidna){
      this.errorMessage = "Broj kartice nije validan"
      return
    }
    this.servis.registrujSe(this.korisnicko_ime, this.lozinka, this.ime, this.prezime, this.pol, this.adresa, this.telefon, this.mejl, this.broj_kartice, this.uloga, this.selectedFile).subscribe(data=>{
      if(data.uspesno){
        alert(data.poruka)
        this.ruter.navigate(["login"])
      }
      else this.errorMessage = data.poruka;
    })
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
