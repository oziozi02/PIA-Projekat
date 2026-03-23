package com.example.backend.models.requests;

import java.util.List;

import com.example.backend.models.Vikendica;

public class NovaVikendicaRequest {
    
    private Vikendica vikendica;
    private List<String> slike;
    private int cenovnikLeto;;
    private int cenovnikZima;
    
    public NovaVikendicaRequest(Vikendica vikendica, List<String> slike, int cenovnikLeto, int cenovnikZima) {
        this.vikendica = vikendica;
        this.slike = slike;
        this.cenovnikLeto = cenovnikLeto;
        this.cenovnikZima = cenovnikZima;
    }

    public Vikendica getVikendica() {
        return vikendica;
    }

    public void setVikendica(Vikendica vikendica) {
        this.vikendica = vikendica;
    }

    public List<String> getSlike() {
        return slike;
    }

    public void setSlike(List<String> slike) {
        this.slike = slike;
    }

    public int getCenovnikLeto() {
        return cenovnikLeto;
    }

    public void setCenovnikLeto(int cenovnikLeto) {
        this.cenovnikLeto = cenovnikLeto;
    }

    public int getCenovnikZima() {
        return cenovnikZima;
    }

    public void setCenovnikZima(int cenovnikZima) {
        this.cenovnikZima = cenovnikZima;
    }

    
}
