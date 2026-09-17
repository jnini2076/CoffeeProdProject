import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Createaccount } from './createaccount';

describe('Createaccount', () => {
  let component: Createaccount;
  let fixture: ComponentFixture<Createaccount>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Createaccount],
    }).compileComponents();

    fixture = TestBed.createComponent(Createaccount);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
