package com.example.backend.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.RezervacijeRepo;
import com.example.backend.models.responses.AktivnaRezervacijaResponse;
import com.example.backend.models.responses.ArhiviranaRezervacijaResponse;
import com.example.backend.models.responses.RezervacijaResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/rezervacije")
public class RezervacijeController {
    
    @GetMapping("/dohvatiAktivneRezervacije")
    public List<AktivnaRezervacijaResponse> dohvatiAktivneRezervacije(@RequestParam String turista) {
        return new RezervacijeRepo().dohvatiAktivneRezervacije(turista);
    }
    
    @GetMapping("/dohvatiMojeRezervacije")
    public List<AktivnaRezervacijaResponse> dohvatiMojeRezervacije(@RequestParam String korisnicko_ime) {
        return new RezervacijeRepo().dohvatiMojeRezervacije(korisnicko_ime);
    }
    
    @PostMapping("/prihvatiRezervaciju")
    public RezervacijaResponse prihvatiRezervaciju(@RequestBody int rezervacijaId) {
        return new RezervacijeRepo().prihvatiRezervaciju(rezervacijaId);
    }
    
    @GetMapping("/odbijRezervaciju")
    public RezervacijaResponse odbijRezervaciju(@RequestParam int rezervacijaId,@RequestParam String komentar) {
        return new RezervacijeRepo().odbijRezervaciju(rezervacijaId, komentar);
    }

    @PostMapping("/otkaziRezervaciju")
    public RezervacijaResponse otkaziRezervaciju(@RequestBody int id) {
        return new RezervacijeRepo().otkaziRezervaciju(id);
    }
    
    @GetMapping("/dohvatiArhiviraneRezervacije")
    public List<ArhiviranaRezervacijaResponse> dohvatiArhiviraneRezervacije(@RequestParam String turista) {
        return new RezervacijeRepo().dohvatiArhiviraneRezervacije(turista);
    }

    @GetMapping("/ostaviKomentar")
    public RezervacijaResponse ostaviKomentar(@RequestParam int id, @RequestParam int ocena, @RequestParam String tekst) {
        return new RezervacijeRepo().ostaviKomentar(id, ocena, tekst);
    }
}