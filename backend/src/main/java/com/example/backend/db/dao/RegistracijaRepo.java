package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.backend.db.DB;
import com.example.backend.models.Korisnik;

public class RegistracijaRepo {

    public boolean zauzetoKorisnickoIme(String korisnicko_ime) {
       try (
           Connection conn = DB.source().getConnection();
           PreparedStatement stm = conn.prepareStatement("SELECT 1 FROM korisnik WHERE korisnicko_ime = ?");
       ) {
           stm.setString(1, korisnicko_ime);
           ResultSet rs = stm.executeQuery();
           return rs.next();
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return true;
    }

    public boolean zabranjenoKorisnickoIme(String korisnicko_ime){
        try(
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("SELECT 1 FROM zabranjeni_korisnici WHERE korisnicko_ime = ?");
        ) {
            stm.setString(1, korisnicko_ime);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean zauzetEmail(String email) {
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("SELECT 1 FROM korisnik WHERE email = ?");
        ) {
            stm.setString(1, email);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean zabranjenEmail(String email){
        try(
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("SELECT 1 FROM zabranjeni_korisnici WHERE email = ?");
        ) {
            stm.setString(1, email);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void registracija(Korisnik k){
        try(
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("INSERT INTO korisnik (korisnicko_ime, lozinka_hash, ime, prezime, pol, adresa, telefon, email, profilna_slika_path, broj_kartice, uloga, aktivan) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        ) {
            stm.setString(1, k.getKorisnicko_ime());
            stm.setString(2, k.getLozinka_hash());
            stm.setString(3, k.getIme());
            stm.setString(4, k.getPrezime());
            stm.setString(5, k.getPol());
            stm.setString(6, k.getAdresa());
            stm.setString(7, k.getTelefon());
            stm.setString(8, k.getMejl());
            stm.setString(9, k.getSlika_putanja());
            stm.setString(10, k.getBroj_kartice());
            stm.setString(11, k.getUloga());
            stm.setBoolean(12, k.isAktivan());
            stm.executeUpdate();
            PreparedStatement stm2 = conn.prepareStatement("INSERT INTO zahtev_za_registraciju (korisnicko_ime) VALUES (?)");
            stm2.setString(1, k.getKorisnicko_ime());
            stm2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
