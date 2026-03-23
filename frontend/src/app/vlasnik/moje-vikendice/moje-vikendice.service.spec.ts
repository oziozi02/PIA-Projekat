import { TestBed } from '@angular/core/testing';
import { MojeVikendiceService } from './moje-vikendice.service';


describe('MojeVikendiceService', () => {
  let service: MojeVikendiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MojeVikendiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
