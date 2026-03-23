package com.example.backend.models.responses;

public class PromenaLozinkeResponse {
    
    private boolean uspesno;
    private String poruka;
    private boolean admin;
    public PromenaLozinkeResponse(boolean uspesno, String poruka, boolean admin) {
        this.uspesno = uspesno;
        this.poruka = poruka;
        this.admin = admin;
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
    public boolean isAdmin() {
        return admin;
    }
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    
}
