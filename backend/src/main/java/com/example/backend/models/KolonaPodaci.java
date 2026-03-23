package com.example.backend.models;

public class KolonaPodaci {
    
    private String mesec;
    private int brojRezervacija;

    public KolonaPodaci(String mesec, int brojRezervacija) {
        this.mesec = mesec;
        this.brojRezervacija = brojRezervacija;
    }

    public String getMesec() {
        return mesec;
    }

    public void setMesec(String mesec) {
        this.mesec = mesec;
    }

    public int getBrojRezervacija() {
        return brojRezervacija;
    }

    public void setBrojRezervacija(int brojRezervacija) {
        this.brojRezervacija = brojRezervacija;
    }

    
}
