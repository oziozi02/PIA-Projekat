package com.example.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.backend.db.DB;

@Service
public class ObradiRezervacije {

    @Scheduled(fixedRate = 60000) // na svakih 60 sekundi
    public void obradiRezervacije(){
        try(Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "INSERT INTO arhiva(rezervacija_id, ocena, datum) " +
                "SELECT t.id, t.ocena, NOW() " +
                "FROM ( " +
                "  SELECT r.id, FLOOR(1 + RAND()*5) as ocena " +
                "  FROM rezervacija r " +
                "  LEFT JOIN arhiva a ON r.id = a.rezervacija_id " +
                "  WHERE a.rezervacija_id IS NULL AND r.datum_do < NOW() AND r.status = 'odobrena' " +
                ") t"
            )) {
                int rowsAffected = stm.executeUpdate();
                PreparedStatement stm3 = conn.prepareStatement(
                    "UPDATE arhiva a " +
                    "SET a.tekst = CASE WHEN a.ocena >= 3 THEN 'Dobra usluga' ELSE 'Loša usluga' END " +
                    "WHERE a.ocena IS NOT NULL AND a.tekst IS NULL"
                );
                stm3.executeUpdate();
                PreparedStatement stm2 = conn.prepareStatement(
                    "INSERT INTO arhiva(rezervacija_id, ocena, tekst, datum)" +
                    "SELECT r.id, NULL, NULL, NOW() " +
                    "FROM rezervacija r " +
                    "LEFT JOIN arhiva a ON r.id = a.rezervacija_id " +
                    "WHERE a.rezervacija_id IS NULL AND r.datum_do < NOW() AND r.status != 'na_cekanju' "
                );
                rowsAffected += stm2.executeUpdate();
                System.out.println("Obradjeno rezervacija: " + rowsAffected);
                PreparedStatement stm4 = conn.prepareStatement(
                    "UPDATE vikendica SET blokirana_do = NULL WHERE blokirana_do IS NOT NULL AND blokirana_do < NOW()"
                );
                int res = stm4.executeUpdate();
                System.out.println("Odblokirano vikendica: " + res);
        } catch (Exception e) {
            System.out.println("Greska pri obradi rezervacija");
            e.printStackTrace();
        }
        /*try(Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "INSERT INTO arhiva(rezervacija_id, ocena, tekst, datum)" +
                "SELECT r.id, NULL, NULL, NOW() " +
                "FROM rezervacija r " +
                "LEFT JOIN arhiva a ON r.id = a.rezervacija_id " +
                "WHERE a.rezervacija_id IS NULL AND r.datum_do < NOW() AND r.status != 'na_cekanju' "
            )) {
                int rowsAffected = stm.executeUpdate();
                System.out.println("Obradjeno rezervacija: " + rowsAffected);
        } catch (Exception e) {
            System.out.println("Greska pri obradi rezervacija");
            e.printStackTrace();
        }*/
    }
    
}
