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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.backend.db.DB;
import com.example.backend.models.Korisnik;
import com.example.backend.models.ZahtevZaRegistraciju;
import com.example.backend.models.responses.RezervacijaResponse;
import com.example.backend.models.responses.VikendicaPosednje3Ocene;

public class AdminRepo {
    
    public List<Korisnik> dohvatiSveKorisnike(){
        List<Korisnik> korisnici = new ArrayList<>();
        try(Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM korisnik WHERE uloga IN ('turista','vlasnik') AND korisnicko_ime NOT IN (SELECT korisnicko_ime FROM zahtev_za_registraciju WHERE status = 'na_cekanju')")){
                ResultSet rs = stm.executeQuery();
                while(rs.next()){
                    korisnici.add(LoginRepo.napraviKorisnika(rs));
                }
                return korisnici;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public RezervacijaResponse promeniAktivacijuKorisnika(String korisnicko_ime, boolean aktivan){
        String sql = "UPDATE korisnik SET aktivan = ? WHERE korisnicko_ime = ?";
        try(Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(sql)){
                stm.setBoolean(1, aktivan);
                stm.setString(2, korisnicko_ime);
                int rowsAffected = stm.executeUpdate();
                if(rowsAffected > 0){
                    return new RezervacijaResponse(true, "Uspesno azuriran korisnik");
                } else {
                    return new RezervacijaResponse(false, "Korisnik ne postoji" );
                }
        } catch (Exception e) {
            e.printStackTrace();
            return new RezervacijaResponse(false, "Greska pri azuriranju korisnika");
        }
    }

    public RezervacijaResponse azurirajKorisnika(String korisnicko_ime, String ime, String prezime, String adresa, String telefon, String email, String broj_kartice, String uloga, boolean aktivan, MultipartFile slika) {
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
             PreparedStatement stm1 = conn.prepareStatement("SELECT profilna_slika_path FROM korisnik WHERE korisnicko_ime = ?")) {
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
                // Samo sigurno ime fajla
                String original = slika.getOriginalFilename();
                String safeName = original == null ? String.valueOf(System.currentTimeMillis())
                        : original.replaceAll("[^a-zA-Z0-9._-]", "_");
                String uniqueName = korisnicko_ime + "_" + safeName;
                // Putanja do resources/static od backenda!!!
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
             PreparedStatement stm2 = conn.prepareStatement("UPDATE korisnik SET ime = ?, prezime = ?, adresa = ?, telefon = ?, email = ?, profilna_slika_path = ?, broj_kartice = ?, uloga = ?, aktivan = ? WHERE korisnicko_ime = ?")) {
            stm2.setString(1, ime);
            stm2.setString(2, prezime);
            stm2.setString(3, adresa);
            stm2.setString(4, telefon);
            stm2.setString(5, email);
            stm2.setString(6, putanjaSlike);
            stm2.setString(7, broj_kartice);
            stm2.setString(8, uloga);
            stm2.setBoolean(9, aktivan);
            stm2.setString(10, korisnicko_ime);
            int rowsUpdated = stm2.executeUpdate();
            if (rowsUpdated > 0) {
                return new RezervacijaResponse(true, "Uspesno azuriran korisnik.");
            } else {
                return new RezervacijaResponse(false, "Korisnik ne postoji.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RezervacijaResponse(false, "Doslo je do greske na serveru.");
        }
    }

    public List<ZahtevZaRegistraciju> dohvatiZahteveZaRegistraciju(){
        List<ZahtevZaRegistraciju> zahtevi = new ArrayList<>();
        try(Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM zahtev_za_registraciju ORDER BY status")){
                ResultSet rs = stm.executeQuery();
                while(rs.next()){
                    ZahtevZaRegistraciju z = new ZahtevZaRegistraciju(
                        rs.getInt("id"),
                        rs.getString("korisnicko_ime"),
                        rs.getString("status"),
                        rs.getString("komentar_odbijanja"),
                        rs.getTimestamp("datum_podnosenja").toLocalDateTime()
                    );
                    zahtevi.add(z);
                }
                return zahtevi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean prihvatiZahtev(int id){
        String sqlUpdate = "UPDATE zahtev_za_registraciju SET status = 'odobren' WHERE id = ?";
        try(Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(sqlUpdate)){
                stm.setInt(1, id);
                int rowsAffected = stm.executeUpdate();
                String sqlKorisnickoIme = "SELECT korisnicko_ime FROM zahtev_za_registraciju WHERE id = ?";
                String korisnicko_ime = null;
                PreparedStatement stm2 = conn.prepareStatement(sqlKorisnickoIme);
                stm2.setInt(1, id);
                ResultSet rs = stm2.executeQuery();
                if(rs.next()){
                    korisnicko_ime = rs.getString("korisnicko_ime");
                }
                else return false;
                String updateKorisnik = "UPDATE korisnik SET aktivan = 1 WHERE korisnicko_ime = ?";
                PreparedStatement stm3 = conn.prepareStatement(updateKorisnik);
                stm3.setString(1, korisnicko_ime);
                int rowsAffected2 = stm3.executeUpdate();
                return rowsAffected > 0 && rowsAffected2 > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean odbijZahtev(int id, String komentar){
        String sql = "UPDATE zahtev_za_registraciju SET status = 'odbijen', komentar_odbijanja = ? WHERE id = ?";
        try(Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(sql)){
                stm.setString(1, komentar);
                stm.setInt(2, id);
                int rowsAffected1 = stm.executeUpdate();
                String korisnicko_ime = null;
                String email = null;
                String slika_putanja = null;
                PreparedStatement stm2 = conn.prepareStatement("SELECT korisnicko_ime FROM zahtev_za_registraciju WHERE id = ?");
                stm2.setInt(1, id);
                ResultSet rs = stm2.executeQuery();
                if(rs.next()){
                    korisnicko_ime = rs.getString("korisnicko_ime");
                }
                else return false;
                PreparedStatement stm3 = conn.prepareStatement("SELECT email FROM korisnik WHERE korisnicko_ime = ?");
                stm3.setString(1, korisnicko_ime);
                ResultSet rs2 = stm3.executeQuery();
                if(rs2.next()){
                    email = rs2.getString("email");
                }
                else return false;
                PreparedStatement stm4 = conn.prepareStatement("SELECT profilna_slika_path FROM korisnik WHERE korisnicko_ime = ?");
                stm4.setString(1, korisnicko_ime);
                ResultSet rs3 = stm4.executeQuery();
                if(rs3.next()){
                    slika_putanja = rs3.getString("profilna_slika_path");
                }
                if(slika_putanja != null){
                    if(!slika_putanja.equals("default.jpg")){
                        Path staticDir = Paths.get("src", "main", "resources", "static");
                        Path target = staticDir.resolve(slika_putanja);
                        Files.deleteIfExists(target);
                    }
                }
                else return false;
                PreparedStatement stm5 = conn.prepareStatement("DELETE FROM korisnik WHERE korisnicko_ime = ?");
                stm5.setString(1, korisnicko_ime);
                int rowsAffected2 = stm5.executeUpdate();
                PreparedStatement stm6 = conn.prepareStatement("INSERT INTO zabranjeni_korisnici (korisnicko_ime, email) VALUES (?, ?)");
                stm6.setString(1, korisnicko_ime);
                stm6.setString(2, email);
                int rowsAffected3 = stm6.executeUpdate();
                return rowsAffected1 > 0 && rowsAffected2 > 0 && rowsAffected3 > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<VikendicaPosednje3Ocene> dohvatiSveVikendice(){
        List<VikendicaPosednje3Ocene> vikendice = new ArrayList<>();
        try(Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM vikendica")){
                ResultSet rs = stm.executeQuery();
                while(rs.next()){
                    int id = rs.getInt("id");
                    String vlasnik = rs.getString("vlasnik");
                    String naziv = rs.getString("naziv");
                    String mesto = rs.getString("mesto");
                    String usluge = rs.getString("usluge");
                    String telefon = rs.getString("telefon");
                    Double lat = rs.getDouble("lat");
                    Double lon = rs.getDouble("lon");
                    Timestamp ts = rs.getTimestamp("blokirana_do");
                    LocalDateTime blokirana_do = ts != null ? ts.toLocalDateTime() : null;
                    boolean poslednje3Ocene = false;
                    String sqlOcene = "SELECT a.ocena " +
                                      "FROM arhiva a " +
                                      "JOIN rezervacija r ON a.rezervacija_id = r.id " +
                                      "JOIN vikendica v ON r.vikendica_id = v.id " +
                                      "WHERE v.id = ? " +
                                      "ORDER BY a.datum DESC " +
                                      "LIMIT 3";
                    PreparedStatement stm2 = conn.prepareStatement(sqlOcene);
                    stm2.setInt(1, id);
                    ResultSet rs2 = stm2.executeQuery();
                    while(rs2.next()){
                        if(rs2.getInt("ocena") > 2 || rs2.getInt("ocena") == 0){
                            poslednje3Ocene = false;
                            break;
                        }
                        else {
                            poslednje3Ocene = true;
                        }
                    }
                    vikendice.add(new VikendicaPosednje3Ocene(id, vlasnik, naziv, mesto, usluge, telefon, lat, lon, blokirana_do, poslednje3Ocene));
                }
                return vikendice;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public RezervacijaResponse blokirajVikendicu(int id){
        try(Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("UPDATE vikendica SET blokirana_do = DATE_ADD(NOW(), INTERVAL 2 DAY) WHERE id = ?")){
                stm.setInt(1, id);
                int rowsAffected = stm.executeUpdate();
                if(rowsAffected > 0){
                    return new RezervacijaResponse(true, "Uspesno blokirana vikendica");
                } else {
                    return new RezervacijaResponse(false, "Vikendica ne postoji" );
                }
        } catch (Exception e) {
            e.printStackTrace();
            return new RezervacijaResponse(false, "Greska pri blokiranju vikendice");
        }
    }
}
