import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { NavComponent } from './nav.component';
import { AuthService } from '../services/auth-service';

describe('NavComponent', () => {
  let fixture: ComponentFixture<NavComponent>;
  let component: NavComponent;
  let auth: any;

  beforeEach(async () => {
    auth = {
      isAdmin: () => false,
      logout: () => {},
    };

    TestBed.resetTestingModule();
    await TestBed.configureTestingModule({
      imports: [NavComponent],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: auth },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(NavComponent);
    component = fixture.componentInstance;
  });

  it('creates the component', () => {
    expect(component).toBeTruthy();
  });

  it('starts closed', () => {
    expect(component.open).toBe(false);
  });

  it('toggles open state', () => {
    component.toggle();
    expect(component.open).toBe(true);
    component.toggle();
    expect(component.open).toBe(false);
  });

  it('closes the panel', () => {
    component.open = true;
    component.close();
    expect(component.open).toBe(false);
  });

  it('shows Users link for all users', () => {
    fixture.detectChanges();
    const links = fixture.nativeElement.querySelectorAll('ul li a');
    const usersLink = Array.from(links).find((l: any) => l.textContent?.trim() === 'Users');
    expect(usersLink).toBeTruthy();
  });

  it('shows Roles link only for admin', () => {
    auth.isAdmin = () => true;
    fixture.detectChanges();

    const links = fixture.nativeElement.querySelectorAll('ul li a');
    const rolesLink = Array.from(links).find((l: any) => l.textContent?.trim() === 'Roles');
    expect(rolesLink).toBeTruthy();
  });

  it('hides Roles link for non-admin', () => {
    auth.isAdmin = () => false;
    fixture.detectChanges();

    const links = fixture.nativeElement.querySelectorAll('ul li a');
    const rolesLink = Array.from(links).find((l: any) => l.textContent?.trim() === 'Roles');
    expect(rolesLink).toBeFalsy();
  });

  it('shows Settings link for all users', () => {
    fixture.detectChanges();
    const links = fixture.nativeElement.querySelectorAll('ul li a');
    const settingsLink = Array.from(links).find((l: any) => l.textContent?.trim() === 'Settings');
    expect(settingsLink).toBeTruthy();
  });

  it('logout closes panel and calls auth.logout', () => {
    let called = false;
    auth.logout = () => { called = true; };
    component.open = true;
    component.logout();
    expect(component.open).toBe(false);
    expect(called).toBe(true);
  });

  it('navigate closes panel', () => {
    component.open = true;
    component.navigate('/home');
    expect(component.open).toBe(false);
  });
});
