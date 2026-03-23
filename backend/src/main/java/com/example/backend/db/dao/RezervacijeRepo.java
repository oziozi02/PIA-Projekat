package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.models.responses.AktivnaRezervacijaResponse;
import com.example.backend.models.responses.ArhiviranaRezervacijaResponse;
import com.example.backend.models.responses.RezervacijaResponse;

public class RezervacijeRepo {
    

    public List<AktivnaRezervacijaResponse> dohvatiAktivneRezervacije(String turista){
        List<AktivnaRezervacijaResponse> aktivneRezervacije = new ArrayList<>();
        try(Connection conn = DB.source().getConnection()){
            String query = "SELECT r.id, v.naziv, v.mesto, r.datum_od, r.datum_do, r.broj_odraslih, r.broj_dece, r.broj_kartice, r.opis, r.status, r.komentar_odbijanja "
                         + "FROM rezervacija r JOIN vikendica v ON r.vikendica_id = v.id "
                         + "WHERE r.turista = ? AND r.status != 'otkazana' AND r.status != 'odbijena' AND r.datum_do >= CURRENT_DATE"
                         + " ORDER BY r.datum_od DESC";
            PreparedStatement stm = conn.prepareStatement(query);
            stm.setString(1, turista);
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                String datumOd = rs.getTimestamp("datum_od").toLocalDateTime().toLocalDate().toString();
                String vremeOd = rs.getTimestamp("datum_od").toLocalDateTime().toLocalTime().toString().substring(0,5);
                String datumDo = rs.getTimestamp("datum_do").toLocalDateTime().toLocalDate().toString();
                String vremeDo = rs.getTimestamp("datum_do").toLocalDateTime().toLocalTime().toString().substring(0,5);
                AktivnaRezervacijaResponse rezervacija = new AktivnaRezervacijaResponse(
                    rs.getInt("id"),
                    rs.getString("naziv"),
                    rs.getString("mesto"),
                    datumOd,
                    datumDo,
                    vremeOd,
                    vremeDo,
                    rs.getInt("broj_odraslih"),
                    rs.getInt("broj_dece"),
                    rs.getString("broj_kartice"),
                    rs.getString("opis"),
                    rs.getString("status"),
                    rs.getString("komentar_odbijanja")
                );
                aktivneRezervacije.add(rezervacija);
            }
            return aktivneRezervacije;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public RezervacijaResponse otkaziRezervaciju(int rezervacija_id){
        try(Connection conn = DB.source().getConnection()){
            String query = "UPDATE rezervacija SET status = 'otkazana' WHERE id = ?";
            PreparedStatement stm = conn.prepareStatement(query);
            stm.setInt(1, rezervacija_id);
            int affectedRows = stm.executeUpdate();
            if(affectedRows > 0){
                return new RezervacijaResponse(true, "Rezervacija je otkazana.");
            } else {
                return new RezervacijaResponse(false, "Rezervacija nije pronadjena.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RezervacijaResponse(false, "Doslo je do greske prilikom otkazivanja rezervacije.");
        }
    }

    public List<ArhiviranaRezervacijaResponse> dohvatiArhiviraneRezervacije(String turista){
        List<ArhiviranaRezervacijaResponse> arhiviraneRezervacije = new ArrayList<>();
        try(Connection conn = DB.source().getConnection()){
            String query = "SELECT a.id, v.naziv, r.datum_od, a.ocena, a.tekst, r.status, r.komentar_odbijanja "
                         + "FROM arhiva a JOIN rezervacija r ON a.rezervacija_id = r.id "
                         + "JOIN vikendica v ON r.vikendica_id = v.id "
                         + "WHERE r.turista = ? "
                         + "ORDER BY r.datum_od DESC";
            PreparedStatement stm = conn.prepareStatement(query);
            stm.setString(1, turista);
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                LocalDateTime datumArhive = rs.getTimestamp("datum_od").toLocalDateTime();
                String datumOd = datumArhive.toLocalDate().toString();
                String vremeOd = datumArhive.toLocalTime().toString().substring(0,5);
                ArhiviranaRezervacijaResponse rezervacija = new ArhiviranaRezervacijaResponse(
                    rs.getInt("id"),
                    rs.getString("naziv"),
                    datumOd,
                    vremeOd,
                    rs.getInt("ocena"),
                    rs.getString("tekst"),
                    rs.getString("status"),
                    rs.getString("komentar_odbijanja")
                );
                arhiviraneRezervacije.add(rezervacija);
            }
            return arhiviraneRezervacije;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public RezervacijaResponse ostaviKomentar(int id, int ocena, String tekst){
        try(Connection conn = DB.source().getConnection()){
            String query = "UPDATE arhiva SET ocena = ?, tekst = ? WHERE id = ?";
            PreparedStatement stm = conn.prepareStatement(query);
            stm.setInt(1, ocena);
            stm.setString(2, tekst);
            stm.setInt(3, id);
            int affectedRows = stm.executeUpdate();
            if(affectedRows > 0){
                return new RezervacijaResponse(true, "Komentar je uspesno sacuvan.");
            } else {
                return new RezervacijaResponse(false, "Rezervacija nije pronadjena.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RezervacijaResponse(false, "Doslo je do greske prilikom ostavljanja komentara.");
        }
    }

    //vlasnik
    public List<AktivnaRezervacijaResponse> dohvatiMojeRezervacije(String vlasnik){
        List<AktivnaRezervacijaResponse> mojeRezervacije = new ArrayList<>();
        try(Connection conn = DB.source().getConnection()){
            String query = "SELECT r.id, v.naziv, v.mesto, r.datum_od, r.datum_do, r.broj_odraslih, r.broj_dece, r.broj_kartice, r.opis, r.status, r.komentar_odbijanja "
                         + "FROM rezervacija r JOIN vikendica v ON r.vikendica_id = v.id JOIN korisnik k ON v.vlasnik = k.korisnicko_ime "
                         + "WHERE k.korisnicko_ime = ? AND r.status != 'otkazana' AND r.status != 'odbijena' "
                         + " ORDER BY r.datum_rezervacije ASC";
            PreparedStatement stm = conn.prepareStatement(query);
            stm.setString(1, vlasnik);
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                String datumOd = rs.getTimestamp("datum_od").toLocalDateTime().toLocalDate().toString();
                String vremeOd = rs.getTimestamp("datum_od").toLocalDateTime().toLocalTime().toString().substring(0,5);
                String datumDo = rs.getTimestamp("datum_do").toLocalDateTime().toLocalDate().toString();
                String vremeDo = rs.getTimestamp("datum_do").toLocalDateTime().toLocalTime().toString().substring(0,5);
                AktivnaRezervacijaResponse rezervacija = new AktivnaRezervacijaResponse(
                    rs.getInt("id"),
                    rs.getString("naziv"),
                    rs.getString("mesto"),
                    datumOd,
                    datumDo,
                    vremeOd,
                    vremeDo,
                    rs.getInt("broj_odraslih"),
                    rs.getInt("broj_dece"),
                    rs.getString("broj_kartice"),
                    rs.getString("opis"),
                    rs.getString("status"),
                    rs.getString("komentar_odbijanja")
                );
                mojeRezervacije.add(rezervacija);
            }
            return mojeRezervacije;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public RezervacijaResponse prihvatiRezervaciju(int rezervacija_id){
        try(Connection conn = DB.source().getConnection()){
            String query = "UPDATE rezervacija SET status = 'odobrena' WHERE id = ?";
            PreparedStatement stm = conn.prepareStatement(query);
            stm.setInt(1, rezervacija_id);
            int affectedRows = stm.executeUpdate();
            if(affectedRows > 0){
                return new RezervacijaResponse(true, "Rezervacija je prihvacena.");
            } else {
                return new RezervacijaResponse(false, "Rezervacija nije pronadjena.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RezervacijaResponse(false, "Doslo je do greske prilikom prihvatanja rezervacije.");
        }
    }

    public RezervacijaResponse odbijRezervaciju(int rezervacija_id, String komentar_odbijanja){
        try(Connection conn = DB.source().getConnection()){
            String query = "UPDATE rezervacija SET status = 'odbijena', komentar_odbijanja = ? WHERE id = ?";
            PreparedStatement stm = conn.prepareStatement(query);
            stm.setString(1, komentar_odbijanja);
            stm.setInt(2, rezervacija_id);
            int affectedRows = stm.executeUpdate();
            if(affectedRows > 0){
                return new RezervacijaResponse(true, "Rezervacija je odbijena.");
            } else {
                return new RezervacijaResponse(false, "Rezervacija nije pronadjena.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RezervacijaResponse(false, "Doslo je do greske prilikom odbijanja rezervacije.");
        }
    }
}
