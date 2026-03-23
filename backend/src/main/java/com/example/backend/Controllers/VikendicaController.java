package com.example.backend.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.VikendicaRepo;
import com.example.backend.models.Cenovnik;
import com.example.backend.models.Komentar;
import com.example.backend.models.Vikendica;
import com.example.backend.models.requests.AzurirajVikendicuRequest;
import com.example.backend.models.requests.NovaVikendicaRequest;
import com.example.backend.models.responses.RezervacijaResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/vikendica")
public class VikendicaController {
    
    @GetMapping("/dohvatiSveVikendice")
    public List<Vikendica> dohvatiSveVikendice() {
        return new VikendicaRepo().dohvatiSveVikendice();
    }
    
    @GetMapping("/pretraziVikendice")
    public List<Vikendica> pretraziVikendice(@RequestParam(required = false) String naziv, @RequestParam(required = false) String mesto) {
        return new VikendicaRepo().pretraziVikendice(naziv, mesto);
    }

    @PostMapping("/dohvatiVikendicuPoId")
    public Vikendica dohvatiVikendicuPoId(@RequestBody int id) {
        return new VikendicaRepo().dohvatiVikendicuPoId(id);
    }

    @PostMapping("/dohvatiSlikeVikendice")
    public List<String> dohvatiSlikeVikendice(@RequestBody int id) {
        return new VikendicaRepo().dohvatiSlikeVikendice(id);
    }

    @PostMapping("/dohvatiCenovnikVikendice")
    public List<Cenovnik> dohvatiCenovnikeVikendice(@RequestBody int id) {
        return new VikendicaRepo().dohvatiCenovnikeVikendice(id);
    }

    @GetMapping("/dohvatiMojeVikendice")
    public List<Vikendica> dohvatiMojeVikendice(@RequestParam String korisnicko_ime) {
        return new VikendicaRepo().dohvatiMojeVikendice(korisnicko_ime);
    }

    @GetMapping("/obrisiVikendicu")
    public RezervacijaResponse obrisiVikendicu(@RequestParam int vikendica_id) {
        return new VikendicaRepo().obrisiVikendicu(vikendica_id);
    }

    @PostMapping("/dodajVikendicu")
    public RezervacijaResponse dodajVikendicu(@RequestBody NovaVikendicaRequest novaVikendica) {
        return new VikendicaRepo().dodajVikendicu(novaVikendica);
    }
    
    @PostMapping("/azurirajVikendicu")
    public RezervacijaResponse azurirajVikendicu(@RequestBody AzurirajVikendicuRequest request) {
        return new VikendicaRepo().azurirajVikendicu(request.getVikendica(), request.getObrisane_slike());
    }

    @PostMapping("/dohvatiKomentareVikendice")
    public List<Komentar> dohvatiKomentareVikendice(@RequestBody int vikendica_id) {
        return new VikendicaRepo().dohvatiKomentareVikendice(vikendica_id);
    }
}
