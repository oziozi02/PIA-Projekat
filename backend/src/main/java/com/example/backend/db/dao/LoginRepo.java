package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

import com.example.backend.db.DB;
import com.example.backend.models.Korisnik;

public class LoginRepo {
    
    public Korisnik login(Korisnik k){
        try(
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM korisnik WHERE korisnicko_ime = ? AND uloga IN ('turista','vlasnik') AND aktivan = 1");
        ) {
            stm.setString(1, k.getKorisnicko_ime());
            ResultSet rs = stm.executeQuery();

            if(rs.next()){
                String storedHash = rs.getString("lozinka_hash");
                if (!BCrypt.checkpw(k.getLozinka_hash(), storedHash)) {
                    return null; // pogresna lozinka
                }
                Korisnik korisnik = napraviKorisnika(rs);
                korisnik.setLozinka_hash("");
                return korisnik;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Korisnik adminLogin(Korisnik k){
        try(
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM korisnik WHERE korisnicko_ime = ? AND uloga = 'administrator' AND aktivan = 1");
        ) {
            stm.setString(1, k.getKorisnicko_ime());
            ResultSet rs = stm.executeQuery();

            if(rs.next()){
                String storedHash = rs.getString("lozinka_hash");
                if (!BCrypt.checkpw(k.getLozinka_hash(), storedHash)) {
                    return null; // pogresna lozinka
                }
                Korisnik korisnik = napraviKorisnika(rs);
                korisnik.setLozinka_hash("");
                return korisnik;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Korisnik napraviKorisnika(ResultSet rs) throws SQLException{
        String korisnicko_ime = rs.getString("korisnicko_ime");
        String lozinka_hash = rs.getString("lozinka_hash");
        String ime = rs.getString("ime");
        String prezime = rs.getString("prezime");
        String pol = rs.getString("pol");
        String adresa = rs.getString("adresa");
        String telefon = rs.getString("telefon");
        String email = rs.getString("email");
        String profilna_slika_path = rs.getString("profilna_slika_path");
        String broj_kartice = rs.getString("broj_kartice");
        String uloga = rs.getString("uloga");
        boolean aktivan = rs.getBoolean("aktivan");

        return new Korisnik(korisnicko_ime, lozinka_hash, ime, prezime, pol, adresa, telefon, email, profilna_slika_path, broj_kartice, uloga, aktivan);
    }

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12)); // cost 12
    }
}
