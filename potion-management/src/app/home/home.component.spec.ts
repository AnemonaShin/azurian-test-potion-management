import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HomeComponent } from './home.component';
import { AuthService } from '../services/auth-service';

describe('HomeComponent', () => {
  let fixture: ComponentFixture<HomeComponent>;
  let auth: any;

  beforeEach(async () => {
    auth = { getUsername: () => 'testuser' };

    TestBed.resetTestingModule();
    await TestBed.configureTestingModule({
      imports: [HomeComponent],
      providers: [{ provide: AuthService, useValue: auth }],
    }).compileComponents();

    fixture = TestBed.createComponent(HomeComponent);
  });

  it('displays the username from auth service', () => {
    fixture.detectChanges();

    const h1 = fixture.nativeElement.querySelector('h1');
    expect(h1).toBeTruthy();
    expect(h1.textContent).toContain('testuser');
  });

  it('wraps in a div with home-wrapper class', () => {
    fixture.detectChanges();

    const wrapper = fixture.nativeElement.querySelector('.home-wrapper');
    expect(wrapper).toBeTruthy();
  });
});
