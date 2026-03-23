import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AzurirajKorisnikaComponent } from './azuriraj-korisnika.component';

describe('AzurirajKorisnikaComponent', () => {
  let component: AzurirajKorisnikaComponent;
  let fixture: ComponentFixture<AzurirajKorisnikaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AzurirajKorisnikaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AzurirajKorisnikaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
