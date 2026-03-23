package com.example.backend.models.responses;

public class RezervacijaResponse {
    
    private boolean uspeh;
    private String poruka;

    public RezervacijaResponse(boolean uspeh, String poruka) {
        this.uspeh = uspeh;
        this.poruka = poruka;
    }

    public boolean isUspeh() {
        return uspeh;
    }

    public void setUspeh(boolean uspeh) {
        this.uspeh = uspeh;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }
}
