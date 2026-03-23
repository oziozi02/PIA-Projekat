import { TestBed } from '@angular/core/testing';
import { PromenaLozinkeService } from './promena-lozinke.service';

describe('PromenaLozinkeService', () => {
  let service: PromenaLozinkeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PromenaLozinkeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
