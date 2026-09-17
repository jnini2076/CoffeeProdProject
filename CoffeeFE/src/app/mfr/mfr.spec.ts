import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Mfr } from './mfr';

describe('Mfr', () => {
  let component: Mfr;
  let fixture: ComponentFixture<Mfr>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Mfr],
    }).compileComponents();

    fixture = TestBed.createComponent(Mfr);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
