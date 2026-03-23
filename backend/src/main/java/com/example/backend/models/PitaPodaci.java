package com.example.backend.models;

public class PitaPodaci {

    private String naziv;
    private int brojRezervacijaVikendom;
    private int brojRezervacijaRadnimDanom;

    public PitaPodaci(String naziv, int brojRezervacijaVikendom, int brojRezervacijaRadnimDanom) {
        this.naziv = naziv;
        this.brojRezervacijaVikendom = brojRezervacijaVikendom;
        this.brojRezervacijaRadnimDanom = brojRezervacijaRadnimDanom;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getBrojRezervacijaVikendom() {
        return brojRezervacijaVikendom;
    }

    public void setBrojRezervacijaVikendom(int brojRezervacijaVikendom) {
        this.brojRezervacijaVikendom = brojRezervacijaVikendom;
    }

    public int getBrojRezervacijaRadnimDanom() {
        return brojRezervacijaRadnimDanom;
    }

    public void setBrojRezervacijaRadnimDanom(int brojRezervacijaRadnimDanom) {
        this.brojRezervacijaRadnimDanom = brojRezervacijaRadnimDanom;
    }

}
