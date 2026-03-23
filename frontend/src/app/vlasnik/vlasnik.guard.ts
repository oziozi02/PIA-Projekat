import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const vlasnikGuard: CanActivateFn = (route, state) => {
  const korisnik = localStorage.getItem("loggedUser");
    if(korisnik){
      const ulogovanKorisnik = JSON.parse(korisnik);
      if(ulogovanKorisnik.uloga == "vlasnik") return true;
    }
    alert("Morate biti ulogovani kao vlasnik da bi pristupili ovoj stranici");
    const ruter = inject(Router)
    ruter.navigate(["login"]);
    return false;
};
