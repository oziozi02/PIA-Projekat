package com.example.backend.db.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import com.example.backend.db.DB;
import com.example.backend.models.Cenovnik;
import com.example.backend.models.Komentar;
import com.example.backend.models.Vikendica;
import com.example.backend.models.requests.NovaVikendicaRequest;
import com.example.backend.models.responses.RezervacijaResponse;

public class VikendicaRepo {

    public static Vikendica napraviVikendicu(ResultSet rs) throws SQLException{
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

        return new Vikendica(id, vlasnik, naziv, mesto, usluge, telefon, lat, lon, blokirana_do, 0.0);
    }

    public Map<Integer,Double> dohvatiProsecneOcene(){
        Map<Integer,Double> prosecneOcene = new HashMap<>();
        try(
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("""
                     SELECT v.id AS vikendica_id, AVG(a.ocena) AS prosecna_ocena
                    FROM vikendica v
                    JOIN rezervacija r ON v.id = r.vikendica_id
                    JOIN arhiva a ON r.id = a.rezervacija_id
                    GROUP BY v.id
                    """
                
            );
        ) {
            ResultSet rs = stm.executeQuery();

            while(rs.next()){
                prosecneOcene.put(rs.getInt("vikendica_id"), rs.getDouble("prosecna_ocena"));
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return prosecneOcene;
    }
    
    public List<Vikendica> dohvatiSveVikendice(){
        List<Vikendica> vikendice = new ArrayList<>();
        Map<Integer,Double> prosecneOcene = dohvatiProsecneOcene();
        try(
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM vikendica");
        ) {
            ResultSet rs = stm.executeQuery();

            while(rs.next()){
                Vikendica v = napraviVikendicu(rs);
                v.setProsecna_ocena(prosecneOcene.getOrDefault(v.getId(), 0.0));
                vikendice.add(v);

            }
            return vikendice;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public List<Vikendica> pretraziVikendice(String naziv, String mesto){
        List<Vikendica> vikendice = new ArrayList<>();
        Map<Integer,Double> prosecneOcene = dohvatiProsecneOcene();
        StringBuilder sb = new StringBuilder("SELECT * FROM vikendica WHERE 1=1");
        if(naziv != null && !naziv.trim().isEmpty()){
            sb.append(" AND LOWER(naziv) LIKE LOWER(?)");
        }
        if(mesto != null && !mesto.trim().isEmpty()){
            sb.append(" AND LOWER(mesto) LIKE LOWER(?)");
        }
        try(
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(sb.toString());
        ) {
            int index = 1;
            if(naziv != null && !naziv.trim().isEmpty()){
                stm.setString(index++, "%" + naziv + "%");
            }
            if(mesto != null && !mesto.trim().isEmpty()){
                stm.setString(index++, "%" + mesto + "%");
            }
            ResultSet rs = stm.executeQuery();

            while(rs.next()){
                Vikendica v = napraviVikendicu(rs);
                v.setProsecna_ocena(prosecneOcene.getOrDefault(v.getId(), 0.0));
                vikendice.add(v);

            }
            return vikendice;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public Vikendica dohvatiVikendicuPoId(int id){
        Map<Integer,Double> prosecneOcene = dohvatiProsecneOcene();
        try(
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM vikendica WHERE id = ?");
        ) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            if(rs.next()){
                Vikendica v = napraviVikendicu(rs);
                v.setProsecna_ocena(prosecneOcene.getOrDefault(v.getId(), 0.0));
                return v;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public List<String> dohvatiSlikeVikendice(int vikendica_id){
        List<String> slike = new ArrayList<>();
        try(
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("SELECT putanja FROM slika WHERE vikendica_id = ?");
        ) {
            stm.setInt(1, vikendica_id);
            ResultSet rs = stm.executeQuery();

            while(rs.next()){
                slike.add(rs.getString("putanja"));
            }
            return slike;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public List<Cenovnik> dohvatiCenovnikeVikendice(int vikendica_id){
        List<Cenovnik> cenovnici = new ArrayList<>();
        try(
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM cenovnik WHERE vikendica_id = ?");
        ) {
            stm.setInt(1, vikendica_id);
            ResultSet rs = stm.executeQuery();

            while(rs.next()){
                Cenovnik c = new Cenovnik(
                    rs.getInt("id"), 
                    rs.getInt("vikendica_id"), 
                    rs.getString("sezona"), 
                    rs.getDouble("cena"));
                cenovnici.add(c);
            }
            return cenovnici;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public List<Vikendica> dohvatiMojeVikendice(String vlasnik){
        List<Vikendica> mojeVikendice = new ArrayList<>();
        Map<Integer,Double> prosecneOcene = dohvatiProsecneOcene();
        try(
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM vikendica WHERE vlasnik = ?");
        ) {
            stm.setString(1, vlasnik);
            ResultSet rs = stm.executeQuery();

            while(rs.next()){
                Vikendica v = napraviVikendicu(rs);
                v.setProsecna_ocena(prosecneOcene.getOrDefault(v.getId(), 0.0));
                mojeVikendice.add(v);

            }
            return mojeVikendice;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public RezervacijaResponse obrisiVikendicu(int vikendica_id){
        try(Connection conn = DB.source().getConnection()){
            String query1 = "DELETE FROM vikendica WHERE id = ?";
            String query2 = "DELETE FROM slika WHERE vikendica_id = ?";
            String query3 = "DELETE FROM cenovnik WHERE vikendica_id = ?";
            String query4 = "DELETE FROM rezervacija WHERE vikendica_id = ?";
            PreparedStatement stm1 = conn.prepareStatement(query1);
            PreparedStatement stm2 = conn.prepareStatement(query2);
            PreparedStatement stm3 = conn.prepareStatement(query3);
            PreparedStatement stm4 = conn.prepareStatement(query4);
            stm4.setInt(1, vikendica_id);
            stm4.executeUpdate();
            stm3.setInt(1, vikendica_id);
            stm3.executeUpdate();
            stm2.setInt(1, vikendica_id);
            stm2.executeUpdate();
            stm1.setInt(1, vikendica_id);
            stm1.executeUpdate();
            Path folderPath = Paths.get("src", "main", "resources", "static", "vikendice", String.valueOf(vikendica_id));
            if (Files.exists(folderPath)) {
                // Prvo obrisati sve fajlove i podfoldere
                // sa deleteIfExists da ne bi bilo izuzetka ako nesto ne postoji
                try (Stream<Path> walk = Files.walk(folderPath)) {
                    walk.sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                }
            }
            return new RezervacijaResponse(true,"Uspesno obrisana vikendica.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return new RezervacijaResponse(false,"Greska prilikom brisanja vikendice.");
        }
    }

    public RezervacijaResponse dodajVikendicu(NovaVikendicaRequest vikendica){
        int vikendicaId = -1;
        try(Connection conn = DB.source().getConnection()){
            conn.setAutoCommit(false);
            String queryVikendica = """
                INSERT INTO vikendica (vlasnik, naziv, mesto, usluge, telefon, lat, lon)
                VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
            PreparedStatement stm = conn.prepareStatement(queryVikendica, PreparedStatement.RETURN_GENERATED_KEYS);
            stm.setString(1, vikendica.getVikendica().getVlasnik());
            stm.setString(2, vikendica.getVikendica().getNaziv());
            stm.setString(3, vikendica.getVikendica().getMesto());
            stm.setString(4, vikendica.getVikendica().getUsluge());
            stm.setString(5, vikendica.getVikendica().getTelefon());
            stm.setDouble(6, vikendica.getVikendica().getLat());
            stm.setDouble(7, vikendica.getVikendica().getLon());

            stm.executeUpdate();
            ResultSet generatedKeys = stm.getGeneratedKeys();
            if (generatedKeys.next()) {
                vikendicaId = generatedKeys.getInt(1);
            } else {
                conn.rollback();
                return new RezervacijaResponse(false, "Greska prilikom dodavanja vikendice.");
            }
            String queryCenovnik = "INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (?, ?, ?)";
            PreparedStatement stmCenovnik = conn.prepareStatement(queryCenovnik);
            int letoCena = vikendica.getCenovnikLeto();
            int zimaCena = vikendica.getCenovnikZima();
            stmCenovnik.setInt(1, vikendicaId);
            stmCenovnik.setString(2, "leto");
            stmCenovnik.setInt(3, letoCena);
            stmCenovnik.executeUpdate();
            stmCenovnik.setInt(1, vikendicaId);
            stmCenovnik.setString(2, "zima");
            stmCenovnik.setInt(3, zimaCena);
            stmCenovnik.executeUpdate();
            if(vikendica.getSlike() != null && !vikendica.getSlike().isEmpty()){
                Path folderPath = Paths.get("src", "main", "resources", "static", "vikendice", String.valueOf(vikendicaId));
                if(!Files.exists(folderPath)){
                    Files.createDirectories(folderPath);
                }
                for(String slikaPath : vikendica.getSlike()){
                    String fileName = vikendicaId + "." + UUID.randomUUID().toString() + ".jpg";
                    Path targetPath = folderPath.resolve(fileName);
                    byte[] imageBytes = Base64.getDecoder().decode(slikaPath.split(",")[1]);
                    Files.write(targetPath, imageBytes);
                    String dbPath = "vikendice/" + vikendicaId + "/" + targetPath.getFileName().toString();
                    String querySlika = "INSERT INTO slika (vikendica_id, putanja) VALUES (?, ?)";
                    PreparedStatement stmSlika = conn.prepareStatement(querySlika);
                    stmSlika.setInt(1, vikendicaId);
                    stmSlika.setString(2, dbPath);
                    stmSlika.executeUpdate();
                }
            }
            conn.commit();
            return new RezervacijaResponse(true, "Uspesno dodata vikendica.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return new RezervacijaResponse(false, "Greska prilikom dodavanja vikendice.");
        }
    }

    public RezervacijaResponse azurirajVikendicu(NovaVikendicaRequest vikendica, List<String> obrisaneSlike){
        try(Connection conn = DB.source().getConnection()){
            conn.setAutoCommit(false);
            String queryVikendica = """
                UPDATE vikendica
                SET naziv = ?, mesto = ?, usluge = ?, telefon = ?, lat = ?, lon = ?
                WHERE id = ?
            """;
            PreparedStatement stm = conn.prepareStatement(queryVikendica);
            stm.setString(1, vikendica.getVikendica().getNaziv());
            stm.setString(2, vikendica.getVikendica().getMesto());
            stm.setString(3, vikendica.getVikendica().getUsluge());
            stm.setString(4, vikendica.getVikendica().getTelefon());
            stm.setDouble(5, vikendica.getVikendica().getLat());
            stm.setDouble(6, vikendica.getVikendica().getLon());
            stm.setInt(7, vikendica.getVikendica().getId());

            stm.executeUpdate();

            int vikendicaId = vikendica.getVikendica().getId();

            String queryCenovnik = "UPDATE cenovnik SET cena = ? WHERE vikendica_id = ? AND sezona = ?";
            PreparedStatement stmCenovnik = conn.prepareStatement(queryCenovnik);
            int letoCena = vikendica.getCenovnikLeto();
            int zimaCena = vikendica.getCenovnikZima();
            stmCenovnik.setInt(1, zimaCena);
            stmCenovnik.setInt(2, vikendicaId);
            stmCenovnik.setString(3, "zima");
            stmCenovnik.executeUpdate();
            stmCenovnik.setInt(1, letoCena);
            stmCenovnik.setInt(2, vikendicaId);
            stmCenovnik.setString(3, "leto");
            stmCenovnik.executeUpdate();

            // Brisanje obrisanih slika iz baze i fajl sistema
            Path folderPath = Paths.get("src", "main", "resources", "static", "vikendice", String.valueOf(vikendicaId));
            if(!Files.exists(folderPath)){
                Files.createDirectories(folderPath);
            }
            if(obrisaneSlike != null){
                for(String slikaPath : obrisaneSlike){
                    Path targetPath = Paths.get("src", "main", "resources", "static", slikaPath);
                    Files.deleteIfExists(targetPath);
                    String queryDeleteSlika = "DELETE FROM slika WHERE vikendica_id = ? AND putanja = ?";
                    PreparedStatement stmDeleteSlika = conn.prepareStatement(queryDeleteSlika);
                    stmDeleteSlika.setInt(1, vikendicaId);
                    stmDeleteSlika.setString(2, slikaPath);
                    stmDeleteSlika.executeUpdate();
                }
            }
            // Dodavanje novih slika
            if(vikendica.getSlike() != null && !vikendica.getSlike().isEmpty()){
                for(String slikaPath : vikendica.getSlike()){
                    String fileName = vikendicaId + "." + UUID.randomUUID().toString() + ".jpg";
                    Path targetPath = folderPath.resolve(fileName);
                    byte[] imageBytes = Base64.getDecoder().decode(slikaPath.split(",")[1]);
                    Files.write(targetPath, imageBytes);
                    String dbPath = "vikendice/" + vikendicaId + "/" + targetPath.getFileName().toString();
                    String querySlika = "INSERT INTO slika (vikendica_id, putanja) VALUES (?, ?)";
                    PreparedStatement stmSlika = conn.prepareStatement(querySlika);
                    stmSlika.setInt(1, vikendicaId);
                    stmSlika.setString(2, dbPath);
                    stmSlika.executeUpdate();
                }
            }

            conn.commit();
            return new RezervacijaResponse(true, "Uspesno azurirana vikendica.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return new RezervacijaResponse(false, "Greska prilikom azuriranja vikendice.");
        }
    }

    public List<Komentar> dohvatiKomentareVikendice(int id) {
        List<Komentar> komentari = new ArrayList<>();
        try(
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT a.ocena, a.tekst, a.datum, r.turista " +
                "FROM arhiva a " +
                "JOIN rezervacija r ON a.rezervacija_id = r.id " +
                "WHERE r.vikendica_id = ? AND a.tekst IS NOT NULL AND a.ocena IS NOT NULL " +
                "ORDER BY a.datum DESC"
            );
        ) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            while(rs.next()){
                Komentar k = new Komentar(
                    rs.getInt("ocena"),
                    rs.getString("tekst"),
                    rs.getTimestamp("datum").toLocalDateTime(),
                    rs.getString("turista")
                );
                komentari.add(k);
            }
            return komentari;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
