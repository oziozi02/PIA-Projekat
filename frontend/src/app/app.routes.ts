import { Routes } from '@angular/router';
import { PocetnaComponent } from './pocetna/pocetna.component';
import { LoginComponent } from './login/login.component';
import { RegistracijaComponent } from './registracija/registracija.component';
import { PromenaLozinkeComponent } from './promena-lozinke/promena-lozinke.component';
import { ProfilComponent as TuristaProfilComponent } from './turista/profil/profil.component';
import { turistaGuard } from './turista/turista.guard';
import { VikendiceComponent as TuristaVikendiceComponent } from './turista/vikendice/vikendice.component';
import { RezervacijeComponent as TuristaRezervacijeComponent } from './turista/rezervacije/rezervacije.component';
import { VikendicaDetaljiComponent } from './turista/vikendice/vikendica-detalji/vikendica-detalji.component';
import { ZakazivanjeComponent } from './turista/zakazivanje/zakazivanje.component';
import { ProfilComponent as VlasnikProfilComponent } from './vlasnik/profil/profil.component';
import { vlasnikGuard } from './vlasnik/vlasnik.guard';
import { RezervacijeComponent as VlasnikRezervacijeComponent } from './vlasnik/rezervacije/rezervacije.component';
import { MojeVikendiceComponent } from './vlasnik/moje-vikendice/moje-vikendice.component';
import { StatistikaComponent } from './vlasnik/statistika/statistika.component';
import { AzurirajVikendicuComponent } from './vlasnik/moje-vikendice/azuriraj-vikendicu/azuriraj-vikendicu.component';
import { AdminLoginComponent } from './admin/admin-login/admin-login.component';
import { KorisniciComponent } from './admin/korisnici/korisnici.component';
import { adminGuard } from './admin/admin.guard';
import { VikendiceComponent as AdminVikendiceComponent } from './admin/vikendice/vikendice.component';
import { ZahteviZaRegistracijuComponent } from './admin/zahtevi-za-registraciju/zahtevi-za-registraciju.component';
import { AzurirajKorisnikaComponent } from './admin/korisnici/azuriraj-korisnika/azuriraj-korisnika.component';

export const routes: Routes = [
    {path: "", component:PocetnaComponent},
    {path: "login", component:LoginComponent},
    {path: "registracija", component:RegistracijaComponent},
    {path: "promeniLozinku", component:PromenaLozinkeComponent},
    {path: "turista/profil", component: TuristaProfilComponent, canActivate: [turistaGuard]},
    {path: "turista/vikendice", component: TuristaVikendiceComponent, canActivate: [turistaGuard]},
    {path: "turista/rezervacije", component: TuristaRezervacijeComponent, canActivate: [turistaGuard]},
    {path: "turista/vikendice/:id", component: VikendicaDetaljiComponent, canActivate: [turistaGuard]},
    {path: "turista/zakazivanje/:id", component: ZakazivanjeComponent, canActivate: [turistaGuard]},
    {path: "vlasnik/profil", component: VlasnikProfilComponent, canActivate: [vlasnikGuard]},
    {path: "vlasnik/rezervacije", component: VlasnikRezervacijeComponent, canActivate: [vlasnikGuard]},
    {path: "vlasnik/moje-vikendice", component: MojeVikendiceComponent, canActivate: [vlasnikGuard]},
    {path: "vlasnik/statistika", component: StatistikaComponent, canActivate: [vlasnikGuard]},
    {path: "vlasnik/azuriraj-vikendicu/:id", component: AzurirajVikendicuComponent, canActivate: [vlasnikGuard]},
    {path: "admin/login", component: AdminLoginComponent},
    {path: "admin/korisnici", component: KorisniciComponent, canActivate: [adminGuard]},
    {path: "admin/vikendice", component: AdminVikendiceComponent, canActivate: [adminGuard]},
    {path: "admin/zahtevi", component: ZahteviZaRegistracijuComponent, canActivate: [adminGuard]},
    {path: "admin/azuriraj-korisnika/:username", component: AzurirajKorisnikaComponent, canActivate: [adminGuard]},
];
