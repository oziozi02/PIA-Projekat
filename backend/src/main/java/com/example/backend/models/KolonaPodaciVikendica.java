package com.example.backend.models;

import java.util.List;

public class KolonaPodaciVikendica {
    
    private String naziv;
    private List<KolonaPodaci> kolonaPodatak;

    public KolonaPodaciVikendica() {}
    public KolonaPodaciVikendica(String naziv, List<KolonaPodaci> kolonaPodatak) {
        this.naziv = naziv;
        this.kolonaPodatak = kolonaPodatak;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public List<KolonaPodaci> getKolonaPodatak() {
        return kolonaPodatak;
    }

    public void setKolonaPodatak(List<KolonaPodaci> kolonaPodatak) {
        this.kolonaPodatak = kolonaPodatak;
    }

    
}
