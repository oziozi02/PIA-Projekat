package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

import com.example.backend.db.DB;
import com.example.backend.models.requests.PromenaLozinkeRequest;
import com.example.backend.models.responses.PromenaLozinkeResponse;

public class PromenaLozinkeRepo {
    
    public PromenaLozinkeResponse promeniLozinku(PromenaLozinkeRequest p){
        try(
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM korisnik WHERE korisnicko_ime = ? AND aktivan = 1");
            PreparedStatement updateStm = conn.prepareStatement("UPDATE korisnik SET lozinka_hash = ? WHERE korisnicko_ime = ?");
            ) {
                stm.setString(1, p.getKorisnicko_ime());
                try (ResultSet rs = stm.executeQuery()) {
                    if(rs.next()){
                        String storedHash = rs.getString("lozinka_hash");
                        if (!BCrypt.checkpw(p.getStaraLozinka(), storedHash)) {
                            return new PromenaLozinkeResponse(false, "Pogresna stara lozinka ili korisnicko ime.", false);
                        }
                        String newHashedPassword = LoginRepo.hashPassword(p.getNovaLozinka());
                        updateStm.setString(1, newHashedPassword);
                        updateStm.setString(2, p.getKorisnicko_ime());
                        int rowsAffected = updateStm.executeUpdate();
                        if(rowsAffected > 0){
                            boolean isAdmin = "administrator".equals(rs.getString("uloga"));
                            return new PromenaLozinkeResponse(true, "Lozinka uspesno promenjena.", isAdmin);
                        } else {
                            return new PromenaLozinkeResponse(false, "Doslo je do greske prilikom promene lozinke.", false);
                        }
                    } else {
                        return new PromenaLozinkeResponse(false, "Pogresna stara lozinka ili korisnicko ime.", false);
                    }
                }
        } catch (SQLException e) {
            e.printStackTrace();
            return new PromenaLozinkeResponse(false, "Doslo je do greske na serveru.", false);
        }
    }
}
