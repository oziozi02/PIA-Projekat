import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const turistaGuard: CanActivateFn = (route, state) => {
  const korisnik = localStorage.getItem("loggedUser");
  if(korisnik){
    const ulogovanKorisnik = JSON.parse(korisnik);
    if(ulogovanKorisnik.uloga == "turista") return true;
  }
  alert("Morate biti ulogovani kao turista da bi pristupili ovoj stranici");
  const ruter = inject(Router)
  ruter.navigate(["login"]);
  return false;
};
