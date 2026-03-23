package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.backend.db.DB;

public class PocetnaRepo {
    
    public int brojVikendica(){
        String sql = "SELECT COUNT(*) FROM vikendica";
        return prebrojElemente(sql);
    }

    public int brojVlasnika(){
        String sql = "SELECT COUNT(*) FROM korisnik WHERE uloga = 'vlasnik' AND korisnicko_ime NOT IN (SELECT korisnicko_ime FROM zahtev_za_registraciju WHERE status = 'na_cekanju')";
        return prebrojElemente(sql);
    }

    public int brojTurista(){
        String sql = "SELECT COUNT(*) FROM korisnik WHERE uloga = 'turista' AND korisnicko_ime NOT IN (SELECT korisnicko_ime FROM zahtev_za_registraciju WHERE status = 'na_cekanju')";
        return prebrojElemente(sql);
    }

    public int brojVikendica24h(){
        String sql = "SELECT COUNT(*) FROM rezervacija WHERE datum_rezervacije >= NOW() - INTERVAL 1 DAY AND datum_rezervacije < NOW()";
        return prebrojElemente(sql);
    }

    public int brojVikendica7d(){
        String sql = "SELECT COUNT(*) FROM rezervacija WHERE datum_rezervacije >= NOW() - INTERVAL 7 DAY AND datum_rezervacije < NOW()";
        return prebrojElemente(sql);
    }

    public int brojVikendica30d(){
        String sql = "SELECT COUNT(*) FROM rezervacija WHERE datum_rezervacije >= NOW() - INTERVAL 30 DAY AND datum_rezervacije < NOW()";
        return prebrojElemente(sql);
    }

    public int prebrojElemente(String sql){
        try(
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(sql);
        ) {
            ResultSet rs = stm.executeQuery();
            if(rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }
}
