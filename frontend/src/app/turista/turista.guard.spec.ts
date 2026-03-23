import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';
import { turistaGuard } from './turista.guard';


describe('turistaGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) =>
      TestBed.runInInjectionContext(() => turistaGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
