package com.example.backend.models;

import java.time.LocalDateTime;

public class Komentar {

    private int ocena;
    private String tekst;
    private LocalDateTime datum;
    private String turista_korisnicko_ime;

    public Komentar(int ocena, String tekst, LocalDateTime datum, String turista_korisnicko_ime) {
        this.ocena = ocena;
        this.tekst = tekst;
        this.datum = datum;
        this.turista_korisnicko_ime = turista_korisnicko_ime;
    }

    public int getOcena() {
        return ocena;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    public String getTurista_korisnicko_ime() {
        return turista_korisnicko_ime;
    }

    public void setTurista_korisnicko_ime(String turista_korisnicko_ime) {
        this.turista_korisnicko_ime = turista_korisnicko_ime;
    }
    
    
}
