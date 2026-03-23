package com.example.backend.db.dao;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.example.backend.db.DB;
import com.example.backend.models.requests.RezervacijaRequest;
import com.example.backend.models.responses.RezervacijaResponse;

public class ZakazivanjeRepo {

    public double izracunajCenuZakazivanja(int vikendica_id, String datumOd, String datumDo, int brojOdraslih, int brojDece) {
       LocalDate datum_od = LocalDate.parse(datumOd);
       LocalDate datum_do = LocalDate.parse(datumDo);
       Map<String, Double> cenovnikMap = new HashMap<>();
       try(Connection conn = DB.source().getConnection()){
            PreparedStatement stm = conn.prepareStatement("SELECT sezona,cena FROM cenovnik WHERE vikendica_id = ?");
            stm.setInt(1, vikendica_id);
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                String sezona = rs.getString("sezona");
                double cena = rs.getDouble("cena");
                cenovnikMap.put(sezona, cena);
            }
       } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
       }
        double ukupnaCena = 0.0;
        double brojOsoba = brojOdraslih + brojDece * 0.5;
        for(LocalDate date = datum_od; !date.isAfter(datum_do); date = date.plusDays(1)){
            int month = date.getMonthValue();
            String sezona = (month >=5 && month <=8) ? "leto" : "zima";
            double dnevnaCena = cenovnikMap.getOrDefault(sezona, 0.0);
            ukupnaCena += dnevnaCena * brojOsoba;
        }
        return ukupnaCena;
    }
    
    public RezervacijaResponse rezervisi(RezervacijaRequest rezervacija){
        try(Connection conn = DB.source().getConnection()){
            PreparedStatement stm = conn.prepareStatement("""
                INSERT INTO rezervacija (vikendica_id, turista, datum_od, datum_do, broj_odraslih, broj_dece, broj_kartice, opis, status, komentar_odbijanja, datum_rezervacije)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, DEFAULT, '', CURRENT_TIMESTAMP)
            """);
            LocalDateTime datumOd = LocalDateTime.parse(rezervacija.getDatum_od() + "T" + rezervacija.getVreme_od());
            LocalDateTime datumDo = LocalDateTime.parse(rezervacija.getDatum_do() + "T" + rezervacija.getVreme_do());
            stm.setInt(1, rezervacija.getVikendica_id());
            stm.setString(2, rezervacija.getKorisnicko_ime());
            stm.setTimestamp(3, Timestamp.valueOf(datumOd));
            stm.setTimestamp(4, Timestamp.valueOf(datumDo));
            stm.setInt(5, rezervacija.getBroj_odraslih());
            stm.setInt(6, rezervacija.getBroj_dece());
            stm.setString(7, rezervacija.getBroj_kartice());
            stm.setString(8, rezervacija.getDodatni_zahtevi());

            int rowsAffected = stm.executeUpdate();
            if(rowsAffected > 0){
                return new RezervacijaResponse(true, "Rezervacija poslata vlasniku.");
            } else {
                return new RezervacijaResponse(false, "Rezervacija neuspesna.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new RezervacijaResponse(false, "Greska prilikom rezervacije.");
        }
    }
}
