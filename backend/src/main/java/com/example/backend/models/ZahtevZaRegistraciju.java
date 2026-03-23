package com.example.backend.models;

import java.time.LocalDateTime;

public class ZahtevZaRegistraciju {
    
    private int id;
    private String korisnicko_ime;
    private String status;
    private String komentar_odbijanja;
    private LocalDateTime datum_podnosenja;

    public ZahtevZaRegistraciju(int id, String korisnicko_ime, String status, String komentar_odbijanja, LocalDateTime datum_podnosenja) {
        this.id = id;
        this.korisnicko_ime = korisnicko_ime;
        this.status = status;
        this.komentar_odbijanja = komentar_odbijanja;
        this.datum_podnosenja = datum_podnosenja;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKorisnicko_ime() {
        return korisnicko_ime;
    }

    public void setKorisnicko_ime(String korisnicko_ime) {
        this.korisnicko_ime = korisnicko_ime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKomentar_odbijanja() {
        return komentar_odbijanja;
    }

    public void setKomentar_odbijanja(String komentar_odbijanja) {
        this.komentar_odbijanja = komentar_odbijanja;
    }

    public LocalDateTime getDatum_podnosenja() {
        return datum_podnosenja;
    }

    public void setDatum_podnosenja(LocalDateTime datum_podnosenja) {
        this.datum_podnosenja = datum_podnosenja;
    }

    
}
