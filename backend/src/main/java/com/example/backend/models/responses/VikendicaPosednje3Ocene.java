package com.example.backend.models.responses;

import java.time.LocalDateTime;

public class VikendicaPosednje3Ocene {
    
    private int id;
    private String vlasnik;
    private String naziv;
    private String mesto;
    private String usluge;
    private String telefon;
    private Double lat;
    private Double lon;
    private LocalDateTime blokirana_do;
    private boolean poslednje3Ocene;

    public VikendicaPosednje3Ocene(int id, String vlasnik, String naziv, String mesto, String usluge, String telefon,
            Double lat, Double lon, LocalDateTime blokirana_do, boolean poslednje3Ocene) {
        this.id = id;
        this.vlasnik = vlasnik;
        this.naziv = naziv;
        this.mesto = mesto;
        this.usluge = usluge;
        this.telefon = telefon;
        this.lat = lat;
        this.lon = lon;
        this.blokirana_do = blokirana_do;
        this.poslednje3Ocene = poslednje3Ocene;
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

    public boolean isPoslednje3Ocene() {
        return poslednje3Ocene;
    }

    public void setPoslednje3Ocene(boolean poslednje3Ocene) {
        this.poslednje3Ocene = poslednje3Ocene;
    }

    
}
