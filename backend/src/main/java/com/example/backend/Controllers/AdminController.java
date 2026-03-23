package com.example.backend.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.db.dao.AdminRepo;
import com.example.backend.models.Korisnik;
import com.example.backend.models.ZahtevZaRegistraciju;
import com.example.backend.models.responses.RezervacijaResponse;
import com.example.backend.models.responses.VikendicaPosednje3Ocene;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;




@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/admin")
public class AdminController {
    
    @GetMapping("/dohvatiSveKorisnike")
    public List<Korisnik> dohvatiSveKorisnike() {
        return new AdminRepo().dohvatiSveKorisnike();
    }

    @GetMapping("/promeniAktivacijuKorisnika")
    public RezervacijaResponse promeniAktivacijuKorisnika(@RequestParam String korisnicko_ime, @RequestParam boolean aktivan) {
        return new AdminRepo().promeniAktivacijuKorisnika(korisnicko_ime, aktivan);
    }
    
    @PostMapping("/azurirajKorisnika")
    public RezervacijaResponse azurirajKorisnika(
        @RequestParam String korisnicko_ime,
        @RequestParam String ime,
        @RequestParam String prezime,
        @RequestParam String adresa,
        @RequestParam String telefon,
        @RequestParam String email,
        @RequestParam String broj_kartice,
        @RequestParam String uloga,
        @RequestParam boolean aktivan,
        @RequestParam(required = false) MultipartFile slika
    ) {  
        return new AdminRepo().azurirajKorisnika(korisnicko_ime, ime, prezime, adresa, telefon, email, broj_kartice, uloga, aktivan, slika);
    }

    @GetMapping("/dohvatiZahteveZaRegistraciju")
    public List<ZahtevZaRegistraciju> dohvatiZahteveZaRegistraciju() {
        return new AdminRepo().dohvatiZahteveZaRegistraciju();
    }

    @GetMapping("/prihvatiZahtev")
    public boolean prihvatiZahtev(@RequestParam int id) {
        return new AdminRepo().prihvatiZahtev(id);
    }
    
    @GetMapping("/odbijZahtev")
    public boolean odbijZahtev(@RequestParam int id, @RequestParam String komentar) {
        return new AdminRepo().odbijZahtev(id, komentar);
    }

    @GetMapping("/dohvatiSveVikendice")
    public List<VikendicaPosednje3Ocene> dohvatiSveVikendice() {
        return new AdminRepo().dohvatiSveVikendice();
    }

    @GetMapping("/blokirajVikendicu")
    public RezervacijaResponse blokirajVikendicu(@RequestParam int id) {
        return new AdminRepo().blokirajVikendicu(id);
    }
}
