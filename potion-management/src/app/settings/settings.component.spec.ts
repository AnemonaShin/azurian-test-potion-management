import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { SettingsComponent } from './settings.component';
import { AuthService } from '../services/auth-service';

describe('SettingsComponent', () => {
  let fixture: ComponentFixture<SettingsComponent>;
  let component: SettingsComponent;
  let httpMock: HttpTestingController;
  let auth: any;
  let storedUsername: string | null;

  beforeEach(async () => {
    storedUsername = null;
    auth = {
      getToken: () => 'test-token',
      getUsername: () => 'testuser',
      getUserId: () => 1,
      updateStoredUsername: (name: string) => { storedUsername = name; },
    };

    TestBed.resetTestingModule();
    await TestBed.configureTestingModule({
      imports: [SettingsComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: AuthService, useValue: auth },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(SettingsComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('creates the component', () => {
    expect(component).toBeTruthy();
  });

  it('shows loading state initially', () => {
    fixture.detectChanges();
    const msg = fixture.nativeElement.querySelector('.msg');
    expect(msg.textContent).toContain('Loading');
    httpMock.expectOne('/users/testuser').flush({ response: { id: 1, username: '', email: '', role: { name: '', roleIcon: '' } } });
  });

  it('loads user profile on init', () => {
    fixture.detectChanges();
    const req = httpMock.expectOne('/users/testuser');
    expect(req.request.method).toBe('GET');
    req.flush({
      response: {
        id: 1,
        username: 'testuser',
        email: 'test@test.com',
        role: { name: 'ADMIN', roleIcon: 'bi-shield-fill' },
      },
    });
    fixture.detectChanges();

    expect(component.loading).toBe(false);
    expect(component.username).toBe('testuser');
    expect(component.email).toBe('test@test.com');
    expect(component.role).toBe('ADMIN');
    expect(component.roleIcon).toBe('bi-shield-fill');
  });

  it('shows error when username is missing', () => {
    auth.getUsername = () => null;
    fixture.detectChanges();

    expect(component.error).toBe('Not authenticated');
    expect(component.loading).toBe(false);
  });

  it('shows error on API failure', () => {
    fixture.detectChanges();
    const req = httpMock.expectOne('/users/testuser');
    req.flush({}, { status: 500, statusText: 'Server Error' });
    fixture.detectChanges();

    expect(component.error).toBe('Failed to load profile');
  });

  it('save sends PUT request and updates stored username', () => {
    fixture.detectChanges();
    httpMock.expectOne('/users/testuser').flush({
      response: { id: 1, username: 'testuser', email: 'test@test.com', role: { name: 'ADMIN', roleIcon: 'bi-shield-fill' } },
    });
    fixture.detectChanges();

    component.username = 'newname';
    component.email = 'new@test.com';
    component.password = 'newpass';
    component.save();

    const req = httpMock.expectOne('/users/1');
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual({
      username: 'newname',
      email: 'new@test.com',
      password: 'newpass',
      role: 'ADMIN',
    });
    req.flush({});

    expect(storedUsername).toBe('newname');
    expect(component.success).toContain('Profile updated');
    expect(component.password).toBe('');
  });

  it('save shows error on failure', () => {
    fixture.detectChanges();
    httpMock.expectOne('/users/testuser').flush({
      response: { id: 1, username: 'testuser', email: 'test@test.com', role: { name: 'ADMIN', roleIcon: 'bi-shield-fill' } },
    });
    fixture.detectChanges();

    component.save();
    const req = httpMock.expectOne('/users/1');
    req.flush({ message: 'USER_DEACTIVATED_UPDATE' }, { status: 400, statusText: 'Bad Request' });

    expect(component.error).toContain('USER_DEACTIVATED_UPDATE');
  });

  it('displays role with icon from utility', () => {
    fixture.detectChanges();
    httpMock.expectOne('/users/testuser').flush({
      response: {
        id: 1,
        username: 'testuser',
        email: 'test@test.com',
        role: { name: 'GARDENER', roleIcon: 'bi-flower1' },
      },
    });
    fixture.detectChanges();

    const i = fixture.nativeElement.querySelector('.role-display i');
    expect(i.className).toContain('bi-flower1');
    expect(fixture.nativeElement.textContent).toContain('GARDENER');
  });
});
