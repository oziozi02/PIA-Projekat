import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AdminService } from '../admin.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-login',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './admin-login.component.html',
  styleUrl: './admin-login.component.css'
})
export class AdminLoginComponent {

  ruter = inject(Router)

  servis = inject(AdminService)

  username = ""
  password = ""
  errorMessage = ""

  prijaviSe(){
    if(this.username == "" || this.password == ""){
      this.errorMessage = "Unesite sve podatke";
      return;
    }
    this.servis.login(this.username, this.password).subscribe(data => {
      if (data == null) {
        this.errorMessage = "Pogresno korisnicko ime ili lozinka";
        return;
      }
      localStorage.setItem("loggedAdmin", JSON.stringify(data));
      this.ruter.navigate(["admin/korisnici"]);
    });
  }

  pocetna(){
    this.ruter.navigate([""]);
  }
}
