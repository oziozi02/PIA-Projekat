package com.example.backend.models;

import java.time.LocalDateTime;

public class Vikendica {
    private int id;
    private String vlasnik;
    private String naziv;
    private String mesto;
    private String usluge;
    private String telefon;
    private Double lat;
    private Double lon;
    private LocalDateTime blokirana_do;
    private Double prosecna_ocena;
    
    public Vikendica(int id, String vlasnik, String naziv, String mesto, String usluge, String telefon, Double lat,
            Double lon, LocalDateTime blokirana_do, Double prosecna_ocena) {
        this.id = id;
        this.vlasnik = vlasnik;
        this.naziv = naziv;
        this.mesto = mesto;
        this.usluge = usluge;
        this.telefon = telefon;
        this.lat = lat;
        this.lon = lon;
        this.blokirana_do = blokirana_do;
        this.prosecna_ocena = prosecna_ocena;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVlasnik() {
        return vlasnik;
    }

    public void setVlasnik(String vlasnik) {
        this.vlasnik = vlasnik;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public String getUsluge() {
        return usluge;
    }

    public void setUsluge(String usluge) {
        this.usluge = usluge;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public LocalDateTime getBlokirana_do() {
        return blokirana_do;
    }

    public void setBlokirana_do(LocalDateTime blokirana_do) {
        this.blokirana_do = blokirana_do;
    }

    public Double getProsecna_ocena() {
        return prosecna_ocena;
    }

    public void setProsecna_ocena(Double prosecna_ocena) {
        this.prosecna_ocena = prosecna_ocena;
    }

    
}
