package com.example.backend.models.requests;

public class PromenaLozinkeRequest {
    
    private String korisnicko_ime;
    private String staraLozinka;
    private String novaLozinka;

    public PromenaLozinkeRequest() {}

    public PromenaLozinkeRequest(String korisnicko_ime, String staraLozinka, String novaLozinka) {
        this.korisnicko_ime = korisnicko_ime;
        this.staraLozinka = staraLozinka;
        this.novaLozinka = novaLozinka;
    }

    public String getKorisnicko_ime() {
        return korisnicko_ime;
    }

    public void setKorisnicko_ime(String korisnicko_ime) {
        this.korisnicko_ime = korisnicko_ime;
    }

    public String getStaraLozinka() {
        return staraLozinka;
    }

    public void setStaraLozinka(String staraLozinka) {
        this.staraLozinka = staraLozinka;
    }

    public String getNovaLozinka() {
        return novaLozinka;
    }

    public void setNovaLozinka(String novaLozinka) {
        this.novaLozinka = novaLozinka;
    }
}
