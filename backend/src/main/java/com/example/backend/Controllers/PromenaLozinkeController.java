package com.example.backend.Controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.PromenaLozinkeRepo;
import com.example.backend.models.requests.PromenaLozinkeRequest;
import com.example.backend.models.responses.PromenaLozinkeResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/promenaLozinke")
public class PromenaLozinkeController {
    
    @PostMapping("")
    public PromenaLozinkeResponse promeniLozinku(@RequestBody PromenaLozinkeRequest entity) {
        return new PromenaLozinkeRepo().promeniLozinku(entity);
    }
    
}
