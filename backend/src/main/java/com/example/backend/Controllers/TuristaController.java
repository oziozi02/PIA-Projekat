package com.example.backend.Controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.db.dao.TuristaRepo;
import com.example.backend.models.Korisnik;
import com.example.backend.models.responses.RezervacijaResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/turista")
public class TuristaController {
    
    @PostMapping("/dohvatiTuristu")
    public Korisnik dohvatiTuristu(@RequestBody String korisnicko_ime) {
        return new TuristaRepo().dohvatiTuristu(korisnicko_ime);
    }

    @PostMapping("/azurirajTuristu")
    public RezervacijaResponse azurirajTuristu(
        @RequestParam String korisnicko_ime,
        @RequestParam String ime,
        @RequestParam String prezime,
        @RequestParam String adresa,
        @RequestParam String telefon,
        @RequestParam String email,
        @RequestParam String broj_kartice,
        @RequestParam(required = false) MultipartFile slika
    ) {
        return new TuristaRepo().azurirajTuristu(korisnicko_ime, ime, prezime, adresa, telefon, email, broj_kartice, slika);
    }
    
    
}
