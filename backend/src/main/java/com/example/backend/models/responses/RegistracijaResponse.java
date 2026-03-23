package com.example.backend.models.responses;

public class RegistracijaResponse {

    private boolean uspesno;
    private String poruka;

    public RegistracijaResponse(boolean uspesno, String poruka) {
        this.uspesno = uspesno;
        this.poruka = poruka;
    }

    public boolean isUspesno() {
        return uspesno;
    }

    public void setUspesno(boolean uspesno) {
        this.uspesno = uspesno;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

}
