package com.example.backend.Controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.LoginRepo;
import com.example.backend.models.Korisnik;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/login")
public class LoginController {

    @PostMapping("")
    public Korisnik login(@RequestBody Korisnik k) {
       return new LoginRepo().login(k);
    }
    
    @PostMapping("/admin")
    public Korisnik adminLogin(@RequestBody Korisnik k) {
       return new LoginRepo().adminLogin(k);
    }
}
