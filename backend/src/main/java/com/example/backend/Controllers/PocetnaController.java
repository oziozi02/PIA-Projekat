package com.example.backend.Controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.PocetnaRepo;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/pocetna")
public class PocetnaController {
    
    @GetMapping("/brojVlasnika")
    public int brojVlasnika() {
        return new PocetnaRepo().brojVlasnika();
    }
    
    @GetMapping("/brojTurista")
    public int brojTurista() {
        return new PocetnaRepo().brojTurista();
    }

    @GetMapping("/brojVikendica")
    public int brojVikendica() {
        return new PocetnaRepo().brojVikendica();
    }

    @GetMapping("/brojVikendica24h")
    public int brojVikendica24h() {
        return new PocetnaRepo().brojVikendica24h();
    }

    @GetMapping("/brojVikendica7d")
    public int brojVikendica7d() {
        return new PocetnaRepo().brojVikendica7d();
    }

     @GetMapping("/brojVikendica30d")
    public int brojVikendica30d() {
        return new PocetnaRepo().brojVikendica30d();
    }
}
