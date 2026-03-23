import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from './login.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  ruter = inject(Router)

  loginService = inject(LoginService)

  username = ""
  password = ""
  errorMessage = ""

  prijaviSe(){
    if(this.username == "" || this.password == ""){
      this.errorMessage = "Unesite korisnicko ime i lozinku"
      return
    }
    this.loginService.login(this.username, this.password).subscribe(data=>{
      if(data == null){
        this.errorMessage = "Pogresno korisnicko ime ili lozinka"
        return
      }
      else{
        localStorage.setItem("loggedUser", JSON.stringify(data))
        if(data.uloga == "turista") this.ruter.navigate(["turista/profil"])
        else if(data.uloga == "vlasnik") this.ruter.navigate(["vlasnik/profil"])
      }
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
