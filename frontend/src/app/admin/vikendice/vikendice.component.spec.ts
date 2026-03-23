import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VikendiceComponent } from './vikendice.component';

describe('VikendiceComponent', () => {
  let component: VikendiceComponent;
  let fixture: ComponentFixture<VikendiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VikendiceComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VikendiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
