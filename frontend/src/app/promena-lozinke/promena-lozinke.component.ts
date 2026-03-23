import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { PromenaLozinkeService } from './promena-lozinke.service';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-promena-lozinke',
  standalone: true,
  imports: [FormsModule, NgIf],
  templateUrl: './promena-lozinke.component.html',
  styleUrl: './promena-lozinke.component.css'
})
export class PromenaLozinkeComponent {

  ruter = inject(Router)

  servis = inject(PromenaLozinkeService)

  username = ""
  password = ""
  newpassword = ""
  newpasswordrepeat = ""
  errorMessage = ""

  proveraLozinke(password: string) {
    const pw = password;
    // Provera lozinke
    if(pw.length < 6 || pw.length > 10) {
      this.errorMessage = "Lozinka mora imati između 6 i 10 karaktera.";
      return;
    }
    if(!/^[A-Za-z]/.test(pw)) {
      this.errorMessage = "Lozinka mora početi slovom.";
      return;
    }
    if(!/[A-Z]/.test(pw)) {
      this.errorMessage = "Lozinka mora imati bar jedno veliko slovo.";
      return;
    }
    if((pw.match(/[a-z]/g) || []).length < 3) {
      this.errorMessage = "Lozinka mora imati bar tri mala slova.";
      return;
    }
    if(!/[0-9]/.test(pw)) {
      this.errorMessage = "Lozinka mora imati bar jedan broj.";
      return;
    }
    if(!/[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]/.test(pw)) {
      this.errorMessage = "Lozinka mora imati bar jedan specijalni karakter.";
      return;
    }
    this.errorMessage = "";
  }

  promeniLozinku(){
    if(this.username == "" || this.password == "" || this.newpassword == "" || this.newpasswordrepeat == ""){
      this.errorMessage = "Unesite sve podatke";
      return;
    }
    if(this.newpassword != this.newpasswordrepeat){
      this.errorMessage = "Lozinke se ne poklapaju";
      return;
    }
    if(this.password == this.newpassword){
      this.errorMessage = "Nova lozinka mora biti različita od stare";
      return;
    }
    if(this.errorMessage != ""){
      this.proveraLozinke(this.newpassword);
      return;
    }
    this.servis.promeniLozinku(this.username, this.password, this.newpassword).subscribe(data=>{
      if(data == null){
        this.errorMessage = "Greska na serveru";
        return;
      }
      if(data.uspesno == false){
        this.errorMessage = data.poruka;
        return;
      }
      if(data.admin)this.ruter.navigate(["admin/login"]);
      else this.ruter.navigate(["login"]);
    });
  }
  pocetna(){
    this.ruter.navigate([""])
  }

  registracija(){
    this.ruter.navigate(["registracija"])
  }

  login(){
    this.ruter.navigate(["login"])
  }

  promenaLozinke(){
    this.ruter.navigate(["promeniLozinku"])
  }
}
