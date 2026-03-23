import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { vikendica } from '../../../models/vikendica';
import { VikendicaService } from '../../../turista/vikendice/vikendica.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MojeVikendiceService } from '../moje-vikendice.service';
import { NovaVikendicaRequest } from '../../../models/requests/NovaVikendicaRequest';

@Component({
  selector: 'app-azuriraj-vikendicu',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './azuriraj-vikendicu.component.html',
  styleUrl: './azuriraj-vikendicu.component.css'
})
export class AzurirajVikendicuComponent implements OnInit {
  ngOnInit(): void {
    const id = this.ruta.snapshot.paramMap.get('id');
    if(id){
      this.vikendicaServis.dohvatiVikendicuPoId(Number(id)).subscribe(data => {
        if(data) {
          this.staraVikendica = data;
          this.vikendicaServis.dohvatiSlikeVikendice(data.id).subscribe(slikeData => {
            this.stareSlike = slikeData
            this.slikeZaPrikaz = slikeData.map(s => `http://localhost:8080/${s}`);

          });
          this.vikendicaServis.dohvatiCenovnikVikendice(data.id).subscribe(cenovnikData => {
            cenovnikData.forEach(c => {
              if(c.sezona === 'leto'){
                this.cenovnikLeto = c.cena;
              } else if(c.sezona === 'zima'){
                this.cenovnikZima = c.cena;
              }
            })
          });
        }
      });
    }
  }



  ruta = inject(ActivatedRoute)

  ruter = inject(Router)

  staraVikendica = new vikendica()

  vikendicaServis = inject(VikendicaService)
  mojeVikendiceServis = inject(MojeVikendiceService)

  stareSlike: string[] = []
  noveSlike: string[] = []
  obrisaneSlike: string[] = []
  slikeZaPrikaz: string[] = []

  cenovnikLeto = 0;
  cenovnikZima = 0;
  errorMessage2 = ""

  onTelefonInput(event: Event) {
    const input = event.target as HTMLInputElement;
    let value = input.value;
    // Only allow + as the first character, rest must be digits
    if (value.startsWith('+')) {
      value = '+' + value.slice(1).replace(/[^\d]/g, '');
    } else {
      value = value.replace(/[^\d]/g, '');
    }
    input.value = value;
    this.staraVikendica.telefon = value;
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      const file = input.files[0];
      const validTypes = ["image/jpeg", "image/png"];
      if (!validTypes.includes(file.type)) {
        this.errorMessage2 = "Slika mora biti u JPG ili PNG formatu.";
        return;
      }
      const files = Array.from(input.files);
      files.forEach(file => {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.slikeZaPrikaz.push(e.target?.result as string);
          this.noveSlike.push(e.target?.result as string);
        };
        reader.readAsDataURL(file);
      });
      this.errorMessage2 = "";
    }
  }

  obrisiSliku(slika: string){
    let staraSlika = this.stareSlike.find(s => `http://localhost:8080/${s}` === slika);
    if(staraSlika){
      this.obrisaneSlike.push(staraSlika);
      this.stareSlike = this.stareSlike.filter(s => s !== staraSlika);
    }
    else{
      this.noveSlike = this.noveSlike.filter(s => s !== slika);
    }
    this.slikeZaPrikaz = this.slikeZaPrikaz.filter(s => s !== slika);
  }

  azurirajVikendicu(){
    if(!this.staraVikendica.naziv || !this.staraVikendica.mesto || !this.staraVikendica.telefon || !this.slikeZaPrikaz.length || this.cenovnikLeto <= 0 || this.cenovnikZima <= 0 || this.staraVikendica.lon == 0 || this.staraVikendica.lat == 0 || !this.staraVikendica.usluge) {
      this.errorMessage2 = "Sva polja su obavezna.";
      return;
    }
    let vikRequest = new NovaVikendicaRequest();
    vikRequest.vikendica = this.staraVikendica;
    vikRequest.slike = this.noveSlike;
    vikRequest.cenovnikLeto = this.cenovnikLeto;
    vikRequest.cenovnikZima = this.cenovnikZima;
    this.mojeVikendiceServis.azurirajVikendicu(vikRequest, this.obrisaneSlike).subscribe(success => {
      if(success.uspeh){
        window.location.reload();
        this.ngOnInit();
      }
      else this.errorMessage2 = success.poruka;
    });
  }


  nazad(){
    this.ruter.navigate([`vlasnik/moje-vikendice`]);
  }
}
