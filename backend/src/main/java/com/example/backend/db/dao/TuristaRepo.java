package com.example.backend.db.dao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.web.multipart.MultipartFile;

import com.example.backend.db.DB;
import com.example.backend.models.Korisnik;
import com.example.backend.models.responses.RezervacijaResponse;

public class TuristaRepo {

    public Korisnik dohvatiTuristu(String korisnicko_ime) {
        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm  = conn.prepareStatement("SELECT * FROM korisnik WHERE korisnicko_ime = ?")) {
            stm.setString(1, korisnicko_ime);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) return LoginRepo.napraviKorisnika(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public RezervacijaResponse azurirajTuristu(String korisnicko_ime, String ime, String prezime,  String adresa, String telefon, String email, String broj_kartice, MultipartFile slika) {
        String prethodnaSlika = null;
        String putanjaSlike = null;
        try(Connection conn = DB.source().getConnection();
            PreparedStatement stmCheckEmail = conn.prepareStatement("SELECT 1 FROM korisnik WHERE email = ? AND korisnicko_ime <> ? ")) {
            stmCheckEmail.setString(1, email);
            stmCheckEmail.setString(2, korisnicko_ime);
            ResultSet rs = stmCheckEmail.executeQuery();
            if (rs.next()) {
                // Email je zauzet od strane drugog korisnika
                return new RezervacijaResponse(false, "Email je zauzet od strane drugog korisnika.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RezervacijaResponse(false, "Doslo je do greske na serveru.");
        }
        try(Connection conn = DB.source().getConnection();
            PreparedStatement zabranjenEmail = conn.prepareStatement("SELECT 1 FROM zabranjeni_korisnici WHERE email = ?")) {
            zabranjenEmail.setString(1, email);
            ResultSet rs = zabranjenEmail.executeQuery();
            if (rs.next()) {
                // Email je zabranjen
                return new RezervacijaResponse(false, "Email je zabranjen.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RezervacijaResponse(false, "Doslo je do greske na serveru.");
        }
        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm1 = conn.prepareStatement("SELECT profilna_slika_path FROM korisnik WHERE korisnicko_ime = ? AND aktivan = 1")) {
            stm1.setString(1, korisnicko_ime);
            ResultSet rs = stm1.executeQuery();
            if (rs.next()) {
                prethodnaSlika = rs.getString("profilna_slika_path");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RezervacijaResponse(false, "Doslo je do greske na serveru.");
        }
        if (slika != null && !slika.isEmpty()) {
            try{
                // Samo bezbedno ime fajla
                String original = slika.getOriginalFilename();
                String safeName = original == null ? String.valueOf(System.currentTimeMillis())
                        : original.replaceAll("[^a-zA-Z0-9._-]", "_");
                String uniqueName = korisnicko_ime + "_" + safeName;
                // Putanja do resources/static foldera iz backenda!!!
                Path staticDir = Paths.get("src", "main", "resources", "static");
                Files.createDirectories(staticDir);

                Path target = staticDir.resolve(uniqueName);

                InputStream in = slika.getInputStream();
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
                putanjaSlike = uniqueName;

                if(prethodnaSlika != null && !prethodnaSlika.equals("default.jpg")){
                    Path target2 = staticDir.resolve(prethodnaSlika);
                    Files.deleteIfExists(target2);
                }
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return new RezervacijaResponse(false, "Doslo je do greske na serveru.");
            }    
        }
        else{
            putanjaSlike = "default.jpg";
            try{
                if(prethodnaSlika != null && !prethodnaSlika.equals("default.jpg")){
                    Path staticDir = Paths.get("src", "main", "resources", "static");
                    Path target = staticDir.resolve(prethodnaSlika);
                    Files.deleteIfExists(target);
                }
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return new RezervacijaResponse(false, "Doslo je do greske na serveru.");
            }
        }
        
        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm2 = conn.prepareStatement("UPDATE korisnik SET ime = ?, prezime = ?, adresa = ?, telefon = ?, email = ?, profilna_slika_path = ?, broj_kartice = ? WHERE korisnicko_ime = ? AND aktivan = 1")) {
            stm2.setString(1, ime);
            stm2.setString(2, prezime);
            stm2.setString(3, adresa);
            stm2.setString(4, telefon);
            stm2.setString(5, email);
            stm2.setString(6, putanjaSlike);
            stm2.setString(7, broj_kartice);
            stm2.setString(8, korisnicko_ime);
            int rowsUpdated = stm2.executeUpdate();
            return new RezervacijaResponse(rowsUpdated > 0, rowsUpdated > 0 ? "Uspešno ažuriranje." : "Ažuriranje nije uspelo.");
        } catch (Exception e) {
            e.printStackTrace();
            return new RezervacijaResponse(false, "Doslo je do greske na serveru.");
        }
    }
}