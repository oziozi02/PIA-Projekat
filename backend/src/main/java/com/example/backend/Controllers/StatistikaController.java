package com.example.backend.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.StatistikaRepo;
import com.example.backend.models.KolonaPodaciVikendica;
import com.example.backend.models.PitaPodaci;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/statistika")
public class StatistikaController {
    
    @GetMapping("/dohvatiPodatkeZaKolone")
    public List<KolonaPodaciVikendica> dohvatiPodatkeZaKolone(@RequestParam String korisnicko_ime) {
        return new StatistikaRepo().dohvatiPodatkeZaKolone(korisnicko_ime);
    }

    @GetMapping("/dohvatiPodatkeZaPitu")
    public List<PitaPodaci> dohvatiPodatkeZaPitu(@RequestParam String korisnicko_ime) {
        return new StatistikaRepo().dohvatiPodatkeZaPite(korisnicko_ime);
    }
}
