package com.example.backend.models;

public class Korisnik {
    
    private String korisnicko_ime;
    private String lozinka_hash;
    private String ime;
    private String prezime;
    private String pol;
    private String adresa;
    private String telefon;
    private String mejl;
    private String slika_putanja;
    private String broj_kartice;
    private String uloga;
    private boolean aktivan;
    
    public Korisnik(String korisnicko_ime, String lozinka_hash, String ime, String prezime, String pol, String adresa,
            String telefon, String email, String slika_putanja, String broj_kartice, String uloga,
            boolean aktivan) {
        this.korisnicko_ime = korisnicko_ime;
        this.lozinka_hash = lozinka_hash;
        this.ime = ime;
        this.prezime = prezime;
        this.pol = pol;
        this.adresa = adresa;
        this.telefon = telefon;
        this.mejl = email;
        this.slika_putanja = slika_putanja;
        this.broj_kartice = broj_kartice;
        this.uloga = uloga;
        this.aktivan = aktivan;
    }

    public String getKorisnicko_ime() {
        return korisnicko_ime;
    }

    public void setKorisnicko_ime(String korisnicko_ime) {
        this.korisnicko_ime = korisnicko_ime;
    }

    public String getLozinka_hash() {
        return lozinka_hash;
    }

    public void setLozinka_hash(String lozinka_hash) {
        this.lozinka_hash = lozinka_hash;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getMejl() {
        return mejl;
    }

    public void setMejl(String email) {
        this.mejl = email;
    }

    public String getSlika_putanja() {
        return slika_putanja;
    }

    public void setSlika_putanja(String profilna_slika_path) {
        this.slika_putanja = profilna_slika_path;
    }

    public String getBroj_kartice() {
        return broj_kartice;
    }

    public void setBroj_kartice(String broj_kartice) {
        this.broj_kartice = broj_kartice;
    }

    public String getUloga() {
        return uloga;
    }

    public void setUloga(String uloga) {
        this.uloga = uloga;
    }

    public boolean isAktivan() {
        return aktivan;
    }

    public void setAktivan(boolean aktivan) {
        this.aktivan = aktivan;
    }

    
}
