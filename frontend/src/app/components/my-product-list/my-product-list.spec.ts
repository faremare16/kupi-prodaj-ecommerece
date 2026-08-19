import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyProductListComponent } from './my-product-list';

describe('MyProductList', () => {
  let component: MyProductListComponent;
  let fixture: ComponentFixture<MyProductListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyProductListComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(MyProductListComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
