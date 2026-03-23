package com.example.backend.models.requests;

import java.util.List;

public class AzurirajVikendicuRequest {
    
    private NovaVikendicaRequest vikendica;
    private List<String> obrisane_slike;

    public AzurirajVikendicuRequest(NovaVikendicaRequest vikendica, List<String> obrisane_slike) {
        this.vikendica = vikendica;
        this.obrisane_slike = obrisane_slike;
    }

    public NovaVikendicaRequest getVikendica() {
        return vikendica;
    }

    public void setVikendica(NovaVikendicaRequest vikendica) {
        this.vikendica = vikendica;
    }

    public List<String> getObrisane_slike() {
        return obrisane_slike;
    }

    public void setObrisane_slike(List<String> obrisane_slike) {
        this.obrisane_slike = obrisane_slike;
    }

    
}
