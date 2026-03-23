package com.example.backend.models.responses;

public class ArhiviranaRezervacijaResponse {
    
    private int id;
    private String naziv;
    private String datum_od;
    private String vreme_od;
    private int ocena;
    private String tekst;
    private String status;
    private String komentar_odbijanja;

    public ArhiviranaRezervacijaResponse(int id, String naziv, String datum_od, String vreme_od, int ocena,
            String tekst, String status, String komentar_odbijanja) {
        this.id = id;
        this.naziv = naziv;
        this.datum_od = datum_od;
        this.vreme_od = vreme_od;
        this.ocena = ocena;
        this.status = status;
        this.tekst = tekst;
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

    public String getDatum_od() {
        return datum_od;
    }

    public void setDatum_od(String datum_od) {
        this.datum_od = datum_od;
    }

    public String getVreme_od() {
        return vreme_od;
    }

    public void setVreme_od(String vreme_od) {
        this.vreme_od = vreme_od;
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
