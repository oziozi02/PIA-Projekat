package com.example.backend.models.requests;

public class RezervacijaRequest {
    
    private int vikendica_id;
    private String korisnicko_ime;
    private String datum_od;
    private String datum_do;
    private String vreme_od;
    private String vreme_do;
    private int broj_odraslih;
    private int broj_dece;
    private String broj_kartice;
    private String dodatni_zahtevi;

    public RezervacijaRequest(int vikendica_id, String korisnicko_ime, String datum_od, String datum_do,
            String vreme_od, String vreme_do, int broj_odraslih, int broj_dece, String broj_kartice, String dodatni_zahtevi) {
        this.vikendica_id = vikendica_id;
        this.korisnicko_ime = korisnicko_ime;
        this.datum_od = datum_od;
        this.datum_do = datum_do;
        this.vreme_od = vreme_od;
        this.vreme_do = vreme_do;
        this.broj_odraslih = broj_odraslih;
        this.broj_dece = broj_dece;
        this.broj_kartice = broj_kartice;
        this.dodatni_zahtevi = dodatni_zahtevi;
    }

    public int getVikendica_id() {
        return vikendica_id;
    }

    public void setVikendica_id(int vikendica_id) {
        this.vikendica_id = vikendica_id;
    }

    public String getKorisnicko_ime() {
        return korisnicko_ime;
    }

    public void setKorisnicko_ime(String korisnicko_ime) {
        this.korisnicko_ime = korisnicko_ime;
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

    public String getDodatni_zahtevi() {
        return dodatni_zahtevi;
    }

    public void setDodatni_zahtevi(String dodatni_zahtevi) {
        this.dodatni_zahtevi = dodatni_zahtevi;
    }

    
}
