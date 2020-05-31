import { TestBed } from '@angular/core/testing';

import { DiceapiService } from './diceapi.service';

describe('DiceapiService', () => {
  let service: DiceapiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DiceapiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
