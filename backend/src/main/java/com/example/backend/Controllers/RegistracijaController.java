package com.example.backend.Controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.db.dao.LoginRepo;
import com.example.backend.db.dao.RegistracijaRepo;
import com.example.backend.models.Korisnik;
import com.example.backend.models.responses.RegistracijaResponse;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/registracija")
public class RegistracijaController {
    
    private RegistracijaRepo registracijaRepo = new RegistracijaRepo();

    @PostMapping
    public RegistracijaResponse registracija(
        @RequestParam String korisnicko_ime,
        @RequestParam String lozinka,
        @RequestParam String ime,
        @RequestParam String prezime,
        @RequestParam String pol,
        @RequestParam String adresa,
        @RequestParam String telefon,
        @RequestParam String email,
        @RequestParam String broj_kartice,
        @RequestParam String uloga,
        @RequestParam(required = false) MultipartFile slika
    
    ){
        RegistracijaResponse response = new RegistracijaResponse(false, "Greska na serveru");
        if(registracijaRepo.zauzetoKorisnickoIme(korisnicko_ime)){
            response.setPoruka("Korisnicko ime je zauzeto");
            return response;
        }
        if(registracijaRepo.zabranjenoKorisnickoIme(korisnicko_ime)){
            response.setPoruka("Korisnicko ime je zabranjeno");
            return response;
        }
        if(registracijaRepo.zauzetEmail(email)){
            response.setPoruka("Email je zauzet");
            return response;
        }
        if(registracijaRepo.zabranjenEmail(email)){
            response.setPoruka("Email je zabranjen");
            return response;
        }
        Korisnik korisnik = new Korisnik(korisnicko_ime, lozinka, ime, prezime, pol, adresa, telefon, email, email, broj_kartice, uloga, false);
        korisnik.setLozinka_hash(LoginRepo.hashPassword(korisnik.getLozinka_hash()));
        String putanjaSlike = "default.jpg";
        if (slika != null && !slika.isEmpty()) {
            try{
                // Samo sigurna imena fajlova
                String original = slika.getOriginalFilename();
                String safeName = original == null ? String.valueOf(System.currentTimeMillis())
                    : original.replaceAll("[^a-zA-Z0-9._-]", "_");

                String uniqueName = korisnicko_ime + "_" + safeName;
                // Putanja do static foldera iz backenda!!!
                Path staticDir = Paths.get("src", "main", "resources", "static");
                Files.createDirectories(staticDir);

                Path target = staticDir.resolve(uniqueName);

                // try-with-resources za automatsko zatvaranje InputStream-a
                try (InputStream in = slika.getInputStream()) {
                    Files.copy(in, target);
                }
                putanjaSlike = uniqueName;
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response.setPoruka("Greska prilikom cuvanja slike");
                return response;
            }    
        }
        korisnik.setSlika_putanja(putanjaSlike);
        registracijaRepo.registracija(korisnik);

        response.setUspesno(true);
        response.setPoruka("Zahtjev za registraciju je uspesno poslat");

        return response;
    }
}
