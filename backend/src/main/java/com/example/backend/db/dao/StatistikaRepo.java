package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.models.KolonaPodaci;
import com.example.backend.models.KolonaPodaciVikendica;
import com.example.backend.models.PitaPodaci;

public class StatistikaRepo {
    
    public List<KolonaPodaciVikendica> dohvatiPodatkeZaKolone(String korisnicko_ime){
        List<KolonaPodaciVikendica> podaci = new ArrayList<>();
        String sql = "SELECT v.id AS vikendica_id, v.naziv, MONTH(r.datum_od) AS mesec, COUNT(*) AS broj_rezervacija " +
                     "FROM vikendica v " +
                     "LEFT JOIN rezervacija r ON v.id = r.vikendica_id AND r.status = 'odobrena' " +
                     "WHERE v.vlasnik = ? " +
                     "GROUP BY v.id, v.naziv, MONTH(r.datum_od) " +
                     "ORDER BY mesec, v.id";
        try(Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(sql);) {
            stm.setString(1, korisnicko_ime);
            ResultSet rs = stm.executeQuery();

            List<String> vikendiceList = new ArrayList<>();
            PreparedStatement stmVikendice = conn.prepareStatement("SELECT naziv FROM vikendica WHERE vlasnik = ?");
            stmVikendice.setString(1, korisnicko_ime);
            ResultSet rsVikendice = stmVikendice.executeQuery();
            while (rsVikendice.next()) {
                vikendiceList.add(rsVikendice.getString("naziv"));
            }
            
            vikendiceList.forEach((naziv) -> {
                List<KolonaPodaci> kolonaPodaciList = new ArrayList<>();
                for (int mesec = 1; mesec <= 12; mesec++) {
                    kolonaPodaciList.add(new KolonaPodaci(dohvatiNazivMeseca(mesec), 0));
                }
                podaci.add(new KolonaPodaciVikendica(naziv, kolonaPodaciList));
            });

            while(rs.next()) {
                String naziv = rs.getString("naziv");
                int mesec = rs.getInt("mesec");
                int brojRezervacija = rs.getInt("broj_rezervacija");
                if(mesec == 0) continue; // preskoci ako nema rezervacija za tu vikendicu
                for (KolonaPodaciVikendica kpv : podaci) {
                    if (kpv.getNaziv().equals(naziv)) {
                        List<KolonaPodaci> kolonaPodaciList = kpv.getKolonaPodatak();
                        kolonaPodaciList.get(mesec - 1).setBrojRezervacija(brojRezervacija);
                        break;
                    }
                }
            }
            return podaci;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PitaPodaci> dohvatiPodatkeZaPite(String korisnicko_ime){
        List<PitaPodaci> podaci = new ArrayList<>();
        String sql = "SELECT v.id AS vikendica_id, v.naziv, " +
                     "SUM(CASE WHEN WEEKDAY(r.datum_od) >= 5 THEN 1 ELSE 0 END) AS vikend, " +
                     "SUM(CASE WHEN WEEKDAY(r.datum_od) < 5 THEN 1 ELSE 0 END) AS radni_dan " +
                     "FROM vikendica v " +
                     "LEFT JOIN rezervacija r ON v.id = r.vikendica_id AND r.status = 'odobrena' " +
                     "WHERE v.vlasnik = ? " +
                     "GROUP BY v.id, v.naziv " +
                     "ORDER BY v.id";
        try(Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(sql);) {
            stm.setString(1, korisnicko_ime);
            ResultSet rs = stm.executeQuery();

            while(rs.next()) {
                String naziv = rs.getString("naziv");
                int vikend = rs.getInt("vikend");
                int radni_dan = rs.getInt("radni_dan");

                podaci.add(new PitaPodaci(naziv, vikend, radni_dan));
            }
            return podaci;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String dohvatiNazivMeseca(int mesec) {
        String[] meseci = {
            "Januar", "Februar", "Mart", "April", "Maj", "Jun",
            "Jul", "Avgust", "Septembar", "Oktobar", "Novembar", "Decembar"
        };
        if (mesec >= 1 && mesec <= 12) {
            return meseci[mesec - 1];
        }
        return null;
    }
}
