package com.example.backend.Controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.ZakazivanjeRepo;
import com.example.backend.models.requests.RezervacijaRequest;
import com.example.backend.models.responses.RezervacijaResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/zakazivanje")
public class ZakazivanjeController {
    
    @GetMapping("/izracunajCenuZakazivanja")
    public double izracunajCenuZakazivanja(@RequestParam int vikendica_id, @RequestParam String datumOd, @RequestParam String datumDo, @RequestParam int brojOdraslih, @RequestParam int brojDece) {
        return new ZakazivanjeRepo().izracunajCenuZakazivanja(vikendica_id, datumOd, datumDo, brojOdraslih, brojDece);
    }
    
    @PostMapping("/potvrdiZakazivanje")
    public RezervacijaResponse potvrdiZakazivanje(@RequestBody RezervacijaRequest rezervacija) {
        return new ZakazivanjeRepo().rezervisi(rezervacija);
    }
    
}
