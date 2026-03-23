package com.example.backend.models.responses;

public class AktivnaRezervacijaResponse {
    
    private int id;
    private String naziv;
    private String mesto;
    private String datum_od;
    private String datum_do;
    private String vreme_od;
    private String vreme_do;
    private int broj_odraslih;
    private int broj_dece;
    private String broj_kartice;
    private String opis;
    private String status;
    private String komentar_odbijanja;
    public AktivnaRezervacijaResponse(int id, String naziv, String mesto, String datum_od, String datum_do,
            String vreme_od, String vreme_do, int broj_odraslih, int broj_dece, String broj_kartice, String opis,
            String status, String komentar_odbijanja) {
        this.id = id;
        this.naziv = naziv;
        this.mesto = mesto;
        this.datum_od = datum_od;
        this.datum_do = datum_do;
        this.vreme_od = vreme_od;
        this.vreme_do = vreme_do;
        this.broj_odraslih = broj_odraslih;
        this.broj_dece = broj_dece;
        this.broj_kartice = broj_kartice;
        this.opis = opis;
        this.status = status;
        this.komentar_odbijanja = komentar_odbijanja;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
    public String getDatum_od() {
        return datum_od;
    }
    public void setDatum_od(String datum_od) {
        this.datum_od = datum_od;
    }
    public String getDatum_do() {
        return datum_do;
    }
    public void setDatum_do(String datum_do) {
        this.datum_do = datum_do;
    }
    public String getVreme_od() {
        return vreme_od;
    }
    public void setVreme_od(String vreme_od) {
        this.vreme_od = vreme_od;
    }
    public String getVreme_do() {
        return vreme_do;
    }
    public void setVreme_do(String vreme_do) {
        this.vreme_do = vreme_do;
    }
    public int getBroj_odraslih() {
        return broj_odraslih;
    }
    public void setBroj_odraslih(int broj_odraslih) {
        this.broj_odraslih = broj_odraslih;
    }
    public int getBroj_dece() {
        return broj_dece;
    }
    public void setBroj_dece(int broj_dece) {
        this.broj_dece = broj_dece;
    }
    public String getBroj_kartice() {
        return broj_kartice;
    }
    public void setBroj_kartice(String broj_kartice) {
        this.broj_kartice = broj_kartice;
    }
    public String getOpis() {
        return opis;
    }
    public void setOpis(String opis) {
        this.opis = opis;
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

    
}
