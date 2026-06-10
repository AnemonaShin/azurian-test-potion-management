import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { LoginComponent } from './login.component';
import { AuthService } from '../services/auth-service';
import { routes } from '../app.routes';

describe('LoginComponent', () => {
  let fixture: ComponentFixture<LoginComponent>;
  let component: LoginComponent;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    localStorage.clear();
    TestBed.resetTestingModule();
    await TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter(routes),
        AuthService,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('creates the component', () => {
    expect(component).toBeTruthy();
  });

  it('shows login form by default', () => {
    const h2 = fixture.nativeElement.querySelector('h2');
    expect(h2.textContent).toContain('Potion Management');
    expect(component.showRegister).toBe(false);
  });

  it('toggles to register form', () => {
    const toggleLink = fixture.nativeElement.querySelector('.toggle-text a');
    toggleLink.click();
    fixture.detectChanges();

    expect(component.showRegister).toBe(true);
    const h2 = fixture.nativeElement.querySelector('h2');
    expect(h2.textContent).toContain('Create Account');
  });

  it('login sends credentials and navigates on success', () => {
    component.username = 'admin';
    component.password = 'pass';
    component.login();

    const req = httpMock.expectOne('/login/');
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Authorization')).toContain('Basic');
    req.flush({
      code: '200',
      message: 'TOKEN GENERATED',
      response: { id: 1, token: 'dG9rZW46cGFzc3dvcmQ=', role: 'ADMIN', username: 'admin' },
    });
  });

  it('login sets error on failure', () => {
    component.username = 'bad';
    component.password = 'creds';
    component.login();

    const req = httpMock.expectOne('/login/');
    req.flush({}, { status: 401, statusText: 'Unauthorized' });

    expect(component.error).toBe('Invalid credentials');
  });

  it('register sends POST and then logs in', () => {
    component.showRegister = true;
    component.regUsername = 'newuser';
    component.regPassword = 'newpass';
    component.regEmail = 'new@test.com';
    component.regRole = 'CRAFTER';

    component.register();

    const regReq = httpMock.expectOne('/users/');
    expect(regReq.request.method).toBe('POST');
    expect(regReq.request.body).toEqual({
      username: 'newuser',
      password: 'newpass',
      email: 'new@test.com',
      role: 'CRAFTER',
    });
    regReq.flush({ code: '200', message: 'USER REGISTERED' });

    const loginReq = httpMock.expectOne('/login/');
    expect(loginReq.request.method).toBe('GET');
    loginReq.flush({
      code: '200',
      message: 'TOKEN GENERATED',
      response: {
        id: 3,
        token: 'bmV3dXNlcjpuZXdwYXNz',
        role: 'CRAFTER',
        username: 'newuser',
      },
    });
  });

  it('register sets error on failure', () => {
    component.showRegister = true;
    component.regUsername = 'dup';
    component.regPassword = 'pass';
    component.regEmail = 'dup@test.com';
    component.regRole = 'GARDENER';

    component.register();

    const req = httpMock.expectOne('/users/');
    req.flush({ message: 'USERNAME DUPLICATION ERROR' }, { status: 400, statusText: 'Bad Request' });

    expect(component.error).toContain('USERNAME DUPLICATION ERROR');
  });
});
