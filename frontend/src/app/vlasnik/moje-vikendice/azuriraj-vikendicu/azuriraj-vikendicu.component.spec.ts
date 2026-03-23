import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AzurirajVikendicuComponent } from './azuriraj-vikendicu.component';

describe('AzurirajVikendicuComponent', () => {
  let component: AzurirajVikendicuComponent;
  let fixture: ComponentFixture<AzurirajVikendicuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AzurirajVikendicuComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AzurirajVikendicuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
