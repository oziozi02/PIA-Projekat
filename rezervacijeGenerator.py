import random
from datetime import datetime, timedelta

korisnici = [
    {"korisnicko_ime": "ognjen", "broj_kartice": "5300515636350461"},
    {"korisnicko_ime": "milan", "broj_kartice": "5512200383661170"},
    {"korisnicko_ime": "branko", "broj_kartice": "4485061214743392"},
    {"korisnicko_ime": "ozren", "broj_kartice": "5166179516859964"},
]

status_options = ['na_cekanju', 'odobrena', 'odbijena', 'otkazana']

opisi = [
    "Molimo za dodatna ćebad i grejanje.",
    "Dodatni jastuci ako je moguće.",
    "Voleli bismo kamin u funkciji.",
    "Molimo da vikendica bude očišćena pre dolaska.",
    "Zamolio bih za korišćenje saune ako postoji.",
    "Dodatne peškire molimo.",
    "Imamo malu decu, potreban je krevetac.",
    "Ako je moguće, pogled na jezero.",
    "Molimo bez kućnih ljubimaca pre nas.",
    "Voleli bismo da imamo roštilj na raspolaganju.",
]

current_year = datetime.now().year
sql_insert = "INSERT INTO rezervacija (vikendica_id, turista, datum_od, datum_do, broj_odraslih, broj_dece, broj_kartice, opis, status, komentar_odbijanja, datum_rezervacije) VALUES\n"

values = []
for _ in range(100):
    korisnik = random.choice(korisnici)
    turista = korisnik["korisnicko_ime"]
    broj_kartice = korisnik["broj_kartice"]
    vikendica_id = random.randint(1, 6)

    start_day = random.randint(1, 330)
    datum_od = datetime(current_year, 1, 1, 12, 0, 0) + timedelta(days=start_day)
    datum_do = datum_od + timedelta(days=random.randint(2, 7), hours=21)
    datum_rezervacije = datum_od - timedelta(days=random.randint(2, 10))

    broj_odraslih = random.randint(1, 4)
    broj_dece = random.randint(0, 3)
    opis = random.choice(opisi)
    status = random.choice(status_options)
    komentar_odbijanja = "'Rezervacija nije odobrena zbog zauzetosti.'" if status == 'odbijena' else "NULL"

    value = f"({vikendica_id}, '{turista}', '{datum_od.strftime('%Y-%m-%d %H:%M:%S')}', '{datum_do.strftime('%Y-%m-%d %H:%M:%S')}', {broj_odraslih}, {broj_dece}, '{broj_kartice}', '{opis}', '{status}', {komentar_odbijanja}, '{datum_rezervacije.strftime('%Y-%m-%d %H:%M:%S')}')"
    values.append(value)

sql_insert += ",\n".join(values) + ";"

print(sql_insert)
