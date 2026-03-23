-- Kreiranje baze ako ne postoji
CREATE DATABASE IF NOT EXISTS PlaninskaVikendica;
USE PlaninskaVikendica;

-- Brisanje starih podataka
DROP TABLE IF EXISTS arhiva;
DROP TABLE IF EXISTS rezervacija;
DROP TABLE IF EXISTS cenovnik;
DROP TABLE IF EXISTS slika;
DROP TABLE IF EXISTS zabranjeni_korisnici;
DROP TABLE IF EXISTS zahtev_za_registraciju;
DROP TABLE IF EXISTS istorija_blokiranja;
DROP TABLE IF EXISTS vikendica;
DROP TABLE IF EXISTS korisnik;

-- Tabela za korisnike
CREATE TABLE korisnik (
    korisnicko_ime VARCHAR(50) PRIMARY KEY,
    lozinka_hash VARCHAR(255) NOT NULL,
    ime VARCHAR(50) NOT NULL,
    prezime VARCHAR(50) NOT NULL,
    pol CHAR(1) CHECK (pol IN ('M', 'Z')),
    adresa VARCHAR(50),
    telefon VARCHAR(20),
    email VARCHAR(50) UNIQUE,
    profilna_slika_path VARCHAR(255),
    broj_kartice VARCHAR(20),
    uloga ENUM('turista', 'vlasnik', 'administrator') NOT NULL,
    aktivan BOOLEAN DEFAULT FALSE
);

-- Tabela za zabranjena korisnicka imena i mejlove
CREATE TABLE zabranjeni_korisnici (
    korisnicko_ime VARCHAR(50) UNIQUE,
    email VARCHAR(50) UNIQUE
);

-- Tabela za zahteve za registraciju
CREATE TABLE zahtev_za_registraciju (
    id INT AUTO_INCREMENT PRIMARY KEY,
    korisnicko_ime VARCHAR(50),
    status VARCHAR(20) DEFAULT 'na_cekanju' CHECK (status IN ('na_cekanju', 'odobren', 'odbijen')),
    komentar_odbijanja VARCHAR(255),
    datum_podnosenja DATETIME DEFAULT NOW()
);

-- Tabela za vikendice
CREATE TABLE vikendica (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vlasnik VARCHAR(50),
    naziv VARCHAR(50),
    mesto VARCHAR(50),
    usluge VARCHAR(50),
    telefon VARCHAR(20),
    lat FLOAT,
    lon FLOAT,
    blokirana_do DATETIME,
    FOREIGN KEY (vlasnik) REFERENCES korisnik(korisnicko_ime)
);

-- Tabela za slike vikendica
CREATE TABLE slika (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vikendica_id INT,
    putanja VARCHAR(255),
    FOREIGN KEY (vikendica_id) REFERENCES vikendica(id)
);

-- Tabela za cenovnik
CREATE TABLE cenovnik (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vikendica_id INT,
    sezona VARCHAR(10) CHECK (sezona IN ('leto', 'zima')),
    cena DECIMAL(10,2),
    FOREIGN KEY (vikendica_id) REFERENCES vikendica(id)
);

-- Tabela za rezervacije
CREATE TABLE rezervacija (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vikendica_id INT,
    turista VARCHAR(50),
    datum_od DATETIME,
    datum_do DATETIME,
    broj_odraslih INT,
    broj_dece INT,
    broj_kartice VARCHAR(20),
    opis TEXT,
    status VARCHAR(20) DEFAULT 'na_cekanju' CHECK (status IN ('na_cekanju', 'odobrena', 'odbijena', 'otkazana')),
    komentar_odbijanja VARCHAR(50),
    datum_rezervacije DATETIME DEFAULT NOW(),
    FOREIGN KEY (vikendica_id) REFERENCES vikendica(id),
    FOREIGN KEY (turista) REFERENCES korisnik(korisnicko_ime)
);

-- Tabela za komentare i ocene
CREATE TABLE arhiva (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rezervacija_id INT,
    ocena INT CHECK (ocena BETWEEN 1 AND 5),
    tekst TEXT,
    datum DATETIME DEFAULT NOW(),
    FOREIGN KEY (rezervacija_id) REFERENCES rezervacija(id)
);

-- Tabela za istoriju blokiranja vikendica
CREATE TABLE istorija_blokiranja (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vikendica_id INT,
    datum_blokiranja DATETIME DEFAULT NOW(),
    razlog VARCHAR(50),
    FOREIGN KEY (vikendica_id) REFERENCES vikendica(id)
);

-- INSERT korisnik
INSERT INTO korisnik (
    korisnicko_ime, lozinka_hash, ime, prezime, pol, adresa, telefon,
    email, profilna_slika_path, broj_kartice, uloga, aktivan
) VALUES
('admin', '$2a$12$ucRrfqKYX9shXc1XMdza3.ZzOQvBf9qsJuM/X8sFmL6YXnwm98Mxm', 'Adminka', 'Adminović', 'Z', 'Džona Kenedija 22', 
'+38162795102', 'admin@gmail.com', 'admin_slika.jpg', '4916524163239799', 'administrator', 1),

('ognjen', '$2a$12$1c8cVOJd39vKWOq44.P1.umg7k5WvWZTjEKNv4g3E5JFap.K8Kh5W', 'Ognjen', 'Jovanović', 'M', 'Momčila Radivojevića 17', 
'0642473581', 'ognjen@gmail.com', 'ognjen_slika.jpg', '5300515636350461', 'turista', 1),

('aleksa', '$2a$12$9AHG60VM3dYlL4sdTOaEVeUT/gzhq4ZQ4IkbFu1AKqSh70pz1ZVxK', 'Aleksa', 'Šućura', 'M', 'Batajnički drum 12', 
'+381623332581', 'aleksa@gmail.com', 'aleksa_slika.jpg', '4716398933162916', 'vlasnik', 1),

('milan', '$2a$12$dI8wLXSd.8BB6L.PS.7/Yu/7VJjTbj1G/Wt42vyQs7Q9ou8NULSzO', 'Milan', 'Andrić', 'M', 'Glavna 206', '0607802552',
 'milan@yahoo.com', 'default.jpg', '5512200383661170', 'turista', 1),

('ana', '$2a$12$Y1BjqGy9MV.DHvefkmEx..QJlM1V4Rmo7bhLbjBbY4W29B9EVmXeO', 'Ana', 'Milić', 'Z', 'Omladinskih brigada 25', '0652271076',
 'ana@gmail.com', 'ana_slika.jpg', '300200383661170', 'vlasnik', 1),

('branko', '$2a$12$1POfn7LPvQrKdbXG7hMzfuTdY8HcQO5pq85UGsoWv3teg6.1wNvlS', 'Branko', 'Jovanović', 'M', 'Antifašističkih borbi 33', '+38162795102',
 'brankojovanovic@gmail.com', 'branko_slika.jpg', '4485061214743392', 'turista', 1),

('nada', '$2a$12$PGzE6GkRetjCUqCKAB6h9OgM.tT5bowSmFouYPZjMvfG/EBqaPwcu', 'Nada', 'Baltić', 'Z', 'Koste Dragičevića 12', '+381605502138',
 'nada@yahoo.com', 'default.jpg', '381689955446289', 'vlasnik', 1),

('ozren', '$2a$12$PCmc4KmyfUOfPEulfWbMBekEYR6X1clS/U5warFI6R0iVlwz/rHi6', 'Ozren', 'Jovanović', 'M', 'Banatska 56', '0655675735',
 'ozren2002@gmail.com', 'ozren_slika.jpg', '5166179516859964', 'turista', 0);
 
 -- INSERT vikendica
 INSERT INTO vikendica (vlasnik, naziv, mesto, usluge, telefon, lat, lon, blokirana_do) VALUES
('aleksa', 'Imanje Savčić', 'Banja Vrujci', 'wifi,parking,bazen', '+381623332581', 44.2243, 20.154, NULL),
('aleksa', 'A Frame Pool House', 'Fruška gora', 'parking,wifi,roštilj', '+381612913599', 45.2057, 19.8097, NULL),
('ana', 'Vikendica Krlić', 'Tara', 'wifi,parking,sadržaj za decu', '+381652271076', 43.8868, 19.4347, NULL),
('ana', 'Planinska kuća Popović', 'Tara', 'wifi,roštilj,aparat za kafu', '+381609122539', 43.9343, 19.4662, NULL),
('nada', 'Apartman Green Hills', 'Novi Sad', 'wifi,klima,bazen', '+381605502138', 45.2411, 19.8093, NULL),
('nada', 'Evergreen Cabins', 'Golija', 'wifi,tv,kuhinja', '0651021235', 43.2865, 20.357, NULL);

-- INSERT slika
INSERT INTO slika (vikendica_id, putanja) VALUES
(1, 'vikendice/1/1.jpg'), (1, 'vikendice/1/2.jpg'), (1, 'vikendice/1/3.jpg'), (1, 'vikendice/1/4.jpg'), (1, 'vikendice/1/5.jpg'), (1, 'vikendice/1/6.jpg'),
(2, 'vikendice/2/1.jpg'), (2, 'vikendice/2/2.jpg'), (2, 'vikendice/2/3.jpg'), (2, 'vikendice/2/4.jpg'), (2, 'vikendice/2/5.jpg'),
(3, 'vikendice/3/1.jpg'), (3, 'vikendice/3/2.jpg'), (3, 'vikendice/3/3.jpg'), (3, 'vikendice/3/4.jpg'), (3, 'vikendice/3/5.jpg'), (3, 'vikendice/3/6.jpg'), (3, 'vikendice/3/7.jpg'),
(4, 'vikendice/4/1.jpg'), (4, 'vikendice/4/2.jpg'), (4, 'vikendice/4/3.jpg'), (4, 'vikendice/4/4.jpg'),
(5, 'vikendice/5/1.jpg'), (5, 'vikendice/5/2.jpg'), (5, 'vikendice/5/3.jpg'), (5, 'vikendice/5/4.jpg'),
(6, 'vikendice/6/1.jpg'), (6, 'vikendice/6/2.jpg'), (6, 'vikendice/6/3.jpg'), (6, 'vikendice/6/4.jpg'), (6, 'vikendice/6/5.jpg'),(6, 'vikendice/6/6.jpg');


-- INSERT cenovnik
-- Imanje Savčić
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (1, 'leto', 1300);
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (1, 'zima', 2000);
-- A Frame Pool House
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (2, 'leto', 1000);
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (2, 'zima', 1800);
-- Vikendica Krlić
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (3, 'leto', 1500);
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (3, 'zima', 2200);
-- Planinska kuća Popović
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (4, 'leto', 1500);
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (4, 'zima', 2000);
-- Apartman Green Hills
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (5, 'leto', 2000);
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (5, 'zima', 2600);
-- Evergreen Cabins
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (6, 'leto', 1250);
INSERT INTO cenovnik (vikendica_id, sezona, cena) VALUES (6, 'zima', 1750);

-- INSERT zabranjeni korisnici
INSERT INTO zabranjeni_korisnici(korisnicko_ime,email) VALUES ('mudja', 'mudja@gmail.com');
INSERT INTO zabranjeni_korisnici(korisnicko_ime,email) VALUES ('miki', 'miki@gmail.com');

INSERT INTO rezervacija (vikendica_id, turista, datum_od, datum_do, broj_odraslih, broj_dece, broj_kartice, opis, status, komentar_odbijanja, datum_rezervacije) VALUES
(3, 'branko', '2025-08-12 12:00:00', '2025-08-17 09:00:00', 3, 0, '4485061214743392', 'Molimo za dodatna ćebad i grejanje.', 'odobrena', NULL, '2025-08-05 12:00:00'),
(3, 'branko', '2025-06-10 12:00:00', '2025-06-18 09:00:00', 2, 3, '4485061214743392', 'Molimo bez kućnih ljubimaca pre nas.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-06-02 12:00:00'),
(3, 'ozren', '2025-04-06 12:00:00', '2025-04-14 09:00:00', 2, 1, '5166179516859964', 'Ako je moguće, pogled na jezero.', 'otkazana', NULL, '2025-03-27 12:00:00'),
(3, 'milan', '2025-04-01 12:00:00', '2025-04-06 09:00:00', 2, 3, '5512200383661170', 'Molimo za dodatna ćebad i grejanje.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-03-25 12:00:00'),
(6, 'branko', '2025-05-07 12:00:00', '2025-05-12 09:00:00', 4, 2, '4485061214743392', 'Dodatni jastuci ako je moguće.', 'odobrena', NULL, '2025-04-27 12:00:00'),
(3, 'branko', '2025-11-21 12:00:00', '2025-11-28 09:00:00', 1, 2, '4485061214743392', 'Molimo da vikendica bude očišćena pre dolaska.', 'na_cekanju', NULL, '2025-11-11 12:00:00'),
(1, 'branko', '2025-07-12 12:00:00', '2025-07-17 09:00:00', 2, 0, '4485061214743392', 'Ako je moguće, pogled na jezero.', 'odobrena', NULL, '2025-07-04 12:00:00'),
(4, 'ozren', '2025-11-07 12:00:00', '2025-11-12 09:00:00', 2, 3, '5166179516859964', 'Dodatni jastuci ako je moguće.', 'na_cekanju', NULL, '2025-10-29 12:00:00'),
(1, 'milan', '2025-03-14 12:00:00', '2025-03-20 09:00:00', 4, 0, '5512200383661170', 'Molimo bez kućnih ljubimaca pre nas.', 'odobrena', NULL, '2025-03-09 12:00:00'),
(6, 'branko', '2025-09-28 12:00:00', '2025-10-02 09:00:00', 4, 3, '4485061214743392', 'Molimo za dodatna ćebad i grejanje.', 'otkazana', NULL, '2025-09-24 12:00:00'),
(4, 'ognjen', '2025-08-18 12:00:00', '2025-08-24 09:00:00', 4, 2, '5300515636350461', 'Molimo za dodatna ćebad i grejanje.', 'odobrena', NULL, '2025-08-14 12:00:00'),
(4, 'branko', '2025-11-02 12:00:00', '2025-11-08 09:00:00', 3, 2, '4485061214743392', 'Imamo malu decu, potreban je krevetac.', 'na_cekanju', NULL, '2025-10-27 12:00:00'),
(1, 'milan', '2025-02-02 12:00:00', '2025-02-07 09:00:00', 2, 0, '5512200383661170', 'Ako je moguće, pogled na jezero.', 'odobrena', NULL, '2025-01-28 12:00:00'),
(2, 'ognjen', '2025-09-02 12:00:00', '2025-09-06 09:00:00', 2, 2, '5300515636350461', 'Ako je moguće, pogled na jezero.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-08-29 12:00:00'),
(1, 'milan', '2025-05-29 12:00:00', '2025-06-03 09:00:00', 3, 0, '5512200383661170', 'Voleli bismo da imamo roštilj na raspolaganju.', 'na_cekanju', NULL, '2025-05-19 12:00:00'),
(4, 'ozren', '2025-11-08 12:00:00', '2025-11-11 09:00:00', 2, 3, '5166179516859964', 'Imamo malu decu, potreban je krevetac.', 'otkazana', NULL, '2025-11-02 12:00:00'),
(3, 'branko', '2025-10-04 12:00:00', '2025-10-09 09:00:00', 4, 0, '4485061214743392', 'Ako je moguće, pogled na jezero.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-09-28 12:00:00'),
(2, 'ognjen', '2025-03-20 12:00:00', '2025-03-24 09:00:00', 4, 2, '5300515636350461', 'Dodatne peškire molimo.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-03-15 12:00:00'),
(4, 'milan', '2025-11-04 12:00:00', '2025-11-08 09:00:00', 1, 3, '5512200383661170', 'Voleli bismo da imamo roštilj na raspolaganju.', 'na_cekanju', NULL, '2025-10-26 12:00:00'),
(5, 'milan', '2025-06-18 12:00:00', '2025-06-21 09:00:00', 1, 0, '5512200383661170', 'Ako je moguće, pogled na jezero.', 'na_cekanju', NULL, '2025-06-12 12:00:00'),
(4, 'ognjen', '2025-06-01 12:00:00', '2025-06-06 09:00:00', 3, 0, '5300515636350461', 'Molimo bez kućnih ljubimaca pre nas.', 'odobrena', NULL, '2025-05-27 12:00:00'),
(5, 'branko', '2025-11-06 12:00:00', '2025-11-14 09:00:00', 2, 2, '4485061214743392', 'Voleli bismo da imamo roštilj na raspolaganju.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-10-29 12:00:00'),
(2, 'branko', '2025-10-14 12:00:00', '2025-10-22 09:00:00', 3, 3, '4485061214743392', 'Voleli bismo kamin u funkciji.', 'otkazana', NULL, '2025-10-10 12:00:00'),
(4, 'ognjen', '2025-07-06 12:00:00', '2025-07-12 09:00:00', 2, 2, '5300515636350461', 'Imamo malu decu, potreban je krevetac.', 'otkazana', NULL, '2025-07-04 12:00:00'),
(2, 'branko', '2025-04-15 12:00:00', '2025-04-23 09:00:00', 1, 3, '4485061214743392', 'Zamolio bih za korišćenje saune ako postoji.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-04-09 12:00:00'),
(5, 'branko', '2025-07-21 12:00:00', '2025-07-29 09:00:00', 2, 3, '4485061214743392', 'Ako je moguće, pogled na jezero.', 'na_cekanju', NULL, '2025-07-11 12:00:00'),
(1, 'branko', '2025-07-23 12:00:00', '2025-07-31 09:00:00', 1, 1, '4485061214743392', 'Zamolio bih za korišćenje saune ako postoji.', 'odobrena', NULL, '2025-07-15 12:00:00'),
(6, 'ozren', '2025-01-13 12:00:00', '2025-01-17 09:00:00', 4, 0, '5166179516859964', 'Voleli bismo kamin u funkciji.', 'otkazana', NULL, '2025-01-07 12:00:00'),
(4, 'branko', '2025-10-04 12:00:00', '2025-10-09 09:00:00', 3, 2, '4485061214743392', 'Voleli bismo kamin u funkciji.', 'odobrena', NULL, '2025-09-26 12:00:00'),
(4, 'ozren', '2025-03-22 12:00:00', '2025-03-28 09:00:00', 1, 3, '5166179516859964', 'Ako je moguće, pogled na jezero.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-03-17 12:00:00'),
(3, 'branko', '2025-05-26 12:00:00', '2025-05-31 09:00:00', 2, 1, '4485061214743392', 'Voleli bismo da imamo roštilj na raspolaganju.', 'na_cekanju', NULL, '2025-05-18 12:00:00'),
(5, 'branko', '2025-04-20 12:00:00', '2025-04-26 09:00:00', 3, 2, '4485061214743392', 'Ako je moguće, pogled na jezero.', 'na_cekanju', NULL, '2025-04-13 12:00:00'),
(5, 'branko', '2025-03-02 12:00:00', '2025-03-05 09:00:00', 3, 3, '4485061214743392', 'Molimo bez kućnih ljubimaca pre nas.', 'otkazana', NULL, '2025-02-21 12:00:00'),
(4, 'milan', '2025-01-19 12:00:00', '2025-01-23 09:00:00', 1, 1, '5512200383661170', 'Molimo da vikendica bude očišćena pre dolaska.', 'odobrena', NULL, '2025-01-13 12:00:00'),
(6, 'ognjen', '2025-04-30 12:00:00', '2025-05-03 09:00:00', 1, 1, '5300515636350461', 'Imamo malu decu, potreban je krevetac.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-04-24 12:00:00'),
(4, 'ognjen', '2025-05-31 12:00:00', '2025-06-06 09:00:00', 1, 0, '5300515636350461', 'Imamo malu decu, potreban je krevetac.', 'otkazana', NULL, '2025-05-28 12:00:00'),
(6, 'ognjen', '2025-02-17 12:00:00', '2025-02-24 09:00:00', 1, 2, '5300515636350461', 'Molimo da vikendica bude očišćena pre dolaska.', 'na_cekanju', NULL, '2025-02-07 12:00:00'),
(4, 'ognjen', '2025-08-22 12:00:00', '2025-08-26 09:00:00', 2, 2, '5300515636350461', 'Molimo da vikendica bude očišćena pre dolaska.', 'odobrena', NULL, '2025-08-19 12:00:00'),
(1, 'milan', '2025-11-11 12:00:00', '2025-11-16 09:00:00', 1, 0, '5512200383661170', 'Dodatne peškire molimo.', 'otkazana', NULL, '2025-11-02 12:00:00'),
(3, 'milan', '2025-03-20 12:00:00', '2025-03-27 09:00:00', 1, 1, '5512200383661170', 'Ako je moguće, pogled na jezero.', 'odobrena', NULL, '2025-03-13 12:00:00'),
(4, 'ognjen', '2025-08-12 12:00:00', '2025-08-19 09:00:00', 2, 0, '5300515636350461', 'Ako je moguće, pogled na jezero.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-08-02 12:00:00'),
(2, 'milan', '2025-02-14 12:00:00', '2025-02-17 09:00:00', 4, 3, '5512200383661170', 'Zamolio bih za korišćenje saune ako postoji.', 'na_cekanju', NULL, '2025-02-11 12:00:00'),
(5, 'branko', '2025-01-09 12:00:00', '2025-01-15 09:00:00', 4, 3, '4485061214743392', 'Ako je moguće, pogled na jezero.', 'odobrena', NULL, '2025-01-06 12:00:00'),
(4, 'ognjen', '2025-08-23 12:00:00', '2025-08-30 09:00:00', 3, 0, '5300515636350461', 'Molimo bez kućnih ljubimaca pre nas.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-08-17 12:00:00'),
(4, 'ozren', '2025-05-13 12:00:00', '2025-05-19 09:00:00', 4, 1, '5166179516859964', 'Voleli bismo kamin u funkciji.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-05-07 12:00:00'),
(3, 'ozren', '2025-10-12 12:00:00', '2025-10-17 09:00:00', 2, 2, '5166179516859964', 'Dodatne peškire molimo.', 'na_cekanju', NULL, '2025-10-04 12:00:00'),
(2, 'branko', '2025-08-24 12:00:00', '2025-08-30 09:00:00', 3, 1, '4485061214743392', 'Voleli bismo kamin u funkciji.', 'odobrena', NULL, '2025-08-19 12:00:00'),
(6, 'milan', '2025-07-08 12:00:00', '2025-07-15 09:00:00', 2, 0, '5512200383661170', 'Voleli bismo da imamo roštilj na raspolaganju.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-07-02 12:00:00'),
(6, 'ognjen', '2025-08-14 12:00:00', '2025-08-22 09:00:00', 4, 0, '5300515636350461', 'Ako je moguće, pogled na jezero.', 'na_cekanju', NULL, '2025-08-08 12:00:00'),
(2, 'ozren', '2025-10-22 12:00:00', '2025-10-30 09:00:00', 4, 2, '5166179516859964', 'Dodatni jastuci ako je moguće.', 'otkazana', NULL, '2025-10-12 12:00:00'),
(2, 'ozren', '2025-03-15 12:00:00', '2025-03-23 09:00:00', 4, 2, '5166179516859964', 'Molimo za dodatna ćebad i grejanje.', 'na_cekanju', NULL, '2025-03-13 12:00:00'),
(4, 'branko', '2025-08-01 12:00:00', '2025-08-09 09:00:00', 1, 0, '4485061214743392', 'Ako je moguće, pogled na jezero.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-07-23 12:00:00'),
(5, 'ognjen', '2025-02-17 12:00:00', '2025-02-20 09:00:00', 1, 3, '5300515636350461', 'Dodatne peškire molimo.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-02-11 12:00:00'),
(2, 'ognjen', '2025-07-11 12:00:00', '2025-07-19 09:00:00', 3, 1, '5300515636350461', 'Ako je moguće, pogled na jezero.', 'na_cekanju', NULL, '2025-07-09 12:00:00'),
(3, 'ozren', '2025-09-21 12:00:00', '2025-09-26 09:00:00', 1, 3, '5166179516859964', 'Molimo da vikendica bude očišćena pre dolaska.', 'odobrena', NULL, '2025-09-15 12:00:00'),
(6, 'branko', '2025-03-02 12:00:00', '2025-03-05 09:00:00', 2, 3, '4485061214743392', 'Imamo malu decu, potreban je krevetac.', 'otkazana', NULL, '2025-02-27 12:00:00'),
(3, 'ognjen', '2025-09-04 12:00:00', '2025-09-08 09:00:00', 1, 3, '5300515636350461', 'Ako je moguće, pogled na jezero.', 'na_cekanju', NULL, '2025-08-26 12:00:00'),
(5, 'branko', '2025-01-20 12:00:00', '2025-01-28 09:00:00', 2, 1, '4485061214743392', 'Molimo da vikendica bude očišćena pre dolaska.', 'otkazana', NULL, '2025-01-10 12:00:00'),
(5, 'ognjen', '2025-02-09 12:00:00', '2025-02-17 09:00:00', 3, 2, '5300515636350461', 'Molimo da vikendica bude očišćena pre dolaska.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-02-04 12:00:00'),
(4, 'ognjen', '2025-07-24 12:00:00', '2025-07-27 09:00:00', 3, 3, '5300515636350461', 'Ako je moguće, pogled na jezero.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-07-18 12:00:00'),
(3, 'ozren', '2025-04-20 12:00:00', '2025-04-28 09:00:00', 1, 3, '5166179516859964', 'Voleli bismo kamin u funkciji.', 'na_cekanju', NULL, '2025-04-13 12:00:00'),
(5, 'ognjen', '2025-08-05 12:00:00', '2025-08-13 09:00:00', 2, 2, '5300515636350461', 'Imamo malu decu, potreban je krevetac.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-07-27 12:00:00'),
(6, 'branko', '2025-10-28 12:00:00', '2025-11-05 09:00:00', 4, 3, '4485061214743392', 'Voleli bismo kamin u funkciji.', 'odobrena', NULL, '2025-10-23 12:00:00'),
(2, 'branko', '2025-04-28 12:00:00', '2025-05-03 09:00:00', 2, 3, '4485061214743392', 'Voleli bismo da imamo roštilj na raspolaganju.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-04-23 12:00:00'),
(3, 'milan', '2025-04-21 12:00:00', '2025-04-27 09:00:00', 2, 1, '5512200383661170', 'Molimo da vikendica bude očišćena pre dolaska.', 'na_cekanju', NULL, '2025-04-16 12:00:00'),
(5, 'ognjen', '2025-09-16 12:00:00', '2025-09-24 09:00:00', 3, 0, '5300515636350461', 'Ako je moguće, pogled na jezero.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-09-11 12:00:00'),
(2, 'ozren', '2025-11-24 12:00:00', '2025-12-01 09:00:00', 1, 2, '5166179516859964', 'Molimo za dodatna ćebad i grejanje.', 'na_cekanju', NULL, '2025-11-20 12:00:00'),
(5, 'ozren', '2025-11-11 12:00:00', '2025-11-17 09:00:00', 4, 2, '5166179516859964', 'Molimo da vikendica bude očišćena pre dolaska.', 'otkazana', NULL, '2025-11-02 12:00:00'),
(1, 'ozren', '2025-10-09 12:00:00', '2025-10-13 09:00:00', 2, 3, '5166179516859964', 'Ako je moguće, pogled na jezero.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-10-06 12:00:00'),
(5, 'ognjen', '2025-09-12 12:00:00', '2025-09-15 09:00:00', 4, 0, '5300515636350461', 'Molimo da vikendica bude očišćena pre dolaska.', 'otkazana', NULL, '2025-09-04 12:00:00'),
(6, 'branko', '2025-07-29 12:00:00', '2025-08-06 09:00:00', 2, 1, '4485061214743392', 'Molimo za dodatna ćebad i grejanje.', 'na_cekanju', NULL, '2025-07-22 12:00:00'),
(1, 'ognjen', '2025-02-25 12:00:00', '2025-03-01 09:00:00', 1, 2, '5300515636350461', 'Molimo bez kućnih ljubimaca pre nas.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-02-19 12:00:00'),
(1, 'ognjen', '2025-07-28 12:00:00', '2025-07-31 09:00:00', 2, 2, '5300515636350461', 'Molimo bez kućnih ljubimaca pre nas.', 'otkazana', NULL, '2025-07-23 12:00:00'),
(5, 'branko', '2025-11-22 12:00:00', '2025-11-26 09:00:00', 1, 0, '4485061214743392', 'Ako je moguće, pogled na jezero.', 'otkazana', NULL, '2025-11-17 12:00:00'),
(4, 'ozren', '2025-05-04 12:00:00', '2025-05-12 09:00:00', 1, 1, '5166179516859964', 'Zamolio bih za korišćenje saune ako postoji.', 'na_cekanju', NULL, '2025-04-30 12:00:00'),
(5, 'ozren', '2025-09-08 12:00:00', '2025-09-11 09:00:00', 4, 0, '5166179516859964', 'Dodatni jastuci ako je moguće.', 'odobrena', NULL, '2025-08-31 12:00:00'),
(6, 'milan', '2025-02-27 12:00:00', '2025-03-04 09:00:00', 3, 1, '5512200383661170', 'Voleli bismo da imamo roštilj na raspolaganju.', 'odobrena', NULL, '2025-02-22 12:00:00'),
(1, 'branko', '2025-08-27 12:00:00', '2025-09-04 09:00:00', 2, 3, '4485061214743392', 'Voleli bismo da imamo roštilj na raspolaganju.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-08-19 12:00:00'),
(5, 'ognjen', '2025-04-09 12:00:00', '2025-04-13 09:00:00', 3, 2, '5300515636350461', 'Molimo bez kućnih ljubimaca pre nas.', 'na_cekanju', NULL, '2025-04-03 12:00:00'),
(1, 'milan', '2025-11-13 12:00:00', '2025-11-18 09:00:00', 1, 2, '5512200383661170', 'Voleli bismo da imamo roštilj na raspolaganju.', 'otkazana', NULL, '2025-11-08 12:00:00'),
(1, 'ozren', '2025-06-07 12:00:00', '2025-06-12 09:00:00', 1, 0, '5166179516859964', 'Imamo malu decu, potreban je krevetac.', 'odobrena', NULL, '2025-06-04 12:00:00'),
(6, 'ozren', '2025-03-12 12:00:00', '2025-03-20 09:00:00', 4, 0, '5166179516859964', 'Molimo za dodatna ćebad i grejanje.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-03-08 12:00:00'),
(1, 'ognjen', '2025-06-12 12:00:00', '2025-06-19 09:00:00', 3, 3, '5300515636350461', 'Imamo malu decu, potreban je krevetac.', 'odobrena', NULL, '2025-06-10 12:00:00'),
(3, 'ognjen', '2025-09-15 12:00:00', '2025-09-19 09:00:00', 4, 3, '5300515636350461', 'Dodatne peškire molimo.', 'odobrena', NULL, '2025-09-10 12:00:00'),
(3, 'ognjen', '2025-02-07 12:00:00', '2025-02-13 09:00:00', 2, 2, '5300515636350461', 'Molimo za dodatna ćebad i grejanje.', 'na_cekanju', NULL, '2025-01-28 12:00:00'),
(2, 'ognjen', '2025-06-12 12:00:00', '2025-06-19 09:00:00', 2, 3, '5300515636350461', 'Zamolio bih za korišćenje saune ako postoji.', 'otkazana', NULL, '2025-06-07 12:00:00'),
(1, 'branko', '2025-04-16 12:00:00', '2025-04-22 09:00:00', 1, 2, '4485061214743392', 'Molimo bez kućnih ljubimaca pre nas.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-04-09 12:00:00'),
(4, 'ozren', '2025-07-01 12:00:00', '2025-07-04 09:00:00', 1, 0, '5166179516859964', 'Dodatni jastuci ako je moguće.', 'otkazana', NULL, '2025-06-21 12:00:00'),
(4, 'milan', '2025-08-13 12:00:00', '2025-08-20 09:00:00', 1, 3, '5512200383661170', 'Dodatne peškire molimo.', 'otkazana', NULL, '2025-08-04 12:00:00'),
(5, 'branko', '2025-01-23 12:00:00', '2025-01-30 09:00:00', 4, 2, '4485061214743392', 'Ako je moguće, pogled na jezero.', 'odobrena', NULL, '2025-01-19 12:00:00'),
(5, 'ognjen', '2025-06-19 12:00:00', '2025-06-22 09:00:00', 1, 2, '5300515636350461', 'Molimo za dodatna ćebad i grejanje.', 'odobrena', NULL, '2025-06-17 12:00:00'),
(6, 'milan', '2025-02-26 12:00:00', '2025-03-05 09:00:00', 3, 1, '5512200383661170', 'Voleli bismo da imamo roštilj na raspolaganju.', 'otkazana', NULL, '2025-02-20 12:00:00'),
(2, 'ognjen', '2025-09-14 12:00:00', '2025-09-22 09:00:00', 2, 1, '5300515636350461', 'Molimo bez kućnih ljubimaca pre nas.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-09-07 12:00:00'),
(6, 'ognjen', '2025-10-25 12:00:00', '2025-10-31 09:00:00', 3, 1, '5300515636350461', 'Voleli bismo da imamo roštilj na raspolaganju.', 'odobrena', NULL, '2025-10-16 12:00:00'),
(3, 'branko', '2025-08-24 12:00:00', '2025-08-29 09:00:00', 3, 2, '4485061214743392', 'Voleli bismo kamin u funkciji.', 'otkazana', NULL, '2025-08-17 12:00:00'),
(5, 'ozren', '2025-11-22 12:00:00', '2025-11-29 09:00:00', 2, 2, '5166179516859964', 'Voleli bismo da imamo roštilj na raspolaganju.', 'na_cekanju', NULL, '2025-11-13 12:00:00'),
(6, 'ognjen', '2025-02-23 12:00:00', '2025-03-03 09:00:00', 2, 0, '5300515636350461', 'Ako je moguće, pogled na jezero.', 'otkazana', NULL, '2025-02-20 12:00:00'),
(4, 'milan', '2025-08-16 12:00:00', '2025-08-19 09:00:00', 3, 1, '5512200383661170', 'Dodatne peškire molimo.', 'odbijena', 'Rezervacija nije odobrena zbog zauzetosti.', '2025-08-14 12:00:00'),
(1, 'milan', '2025-04-21 12:00:00', '2025-04-25 09:00:00', 4, 0, '5512200383661170', 'Molimo da vikendica bude očišćena pre dolaska.', 'otkazana', NULL, '2025-04-17 12:00:00'),
(3, 'branko', '2025-04-25 12:00:00', '2025-04-29 09:00:00', 2, 0, '4485061214743392', 'Molimo bez kućnih ljubimaca pre nas.', 'na_cekanju', NULL, '2025-04-20 12:00:00');


