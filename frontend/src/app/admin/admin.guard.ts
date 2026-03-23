import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const adminGuard: CanActivateFn = (route, state) => {
  const admin = localStorage.getItem("loggedAdmin");
  if (admin) {
    return true;
  } else {
    alert("Morate biti ulogovani kao administrator da bi pristupili ovoj stranici");
    const ruter = inject(Router)
    ruter.navigate(['/admin/login']);
    return false;
  }
};
