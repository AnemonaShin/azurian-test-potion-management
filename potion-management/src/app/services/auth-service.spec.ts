import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { AuthService, LoginResponse } from './auth-service';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    localStorage.clear();
    TestBed.resetTestingModule();
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('returns null when not logged in', () => {
    expect(service.getToken()).toBe(null);
    expect(service.getRole()).toBe(null);
    expect(service.getUsername()).toBe(null);
    expect(service.getUserId()).toBe(null);
    expect(service.isLoggedIn()).toBe(false);
  });

  it('login stores token, role, username, id', () => {
    const mockResponse: LoginResponse = {
      code: '200',
      message: 'TOKEN GENERATED',
      response: {
        id: 1,
        token: 'dG9rZW46cGFzc3dvcmQ=',
        role: 'ADMIN',
        username: 'admin',
      },
    };

    service.login('admin', 'pass').subscribe();

    const req = httpMock.expectOne('/login/');
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);

    expect(service.getToken()).toBe('dG9rZW46cGFzc3dvcmQ=');
    expect(service.getRole()).toBe('ADMIN');
    expect(service.getUsername()).toBe('admin');
    expect(service.getUserId()).toBe(1);
    expect(service.isLoggedIn()).toBe(true);
    expect(service.isAdmin()).toBe(true);
  });

  it('isAdmin returns false for non-admin roles', () => {
    const mockResponse: LoginResponse = {
      code: '200',
      message: 'TOKEN GENERATED',
      response: {
        id: 2,
        token: 'dG9rZW46cGFzc3dvcmQ=',
        role: 'CRAFTER',
        username: 'crafter',
      },
    };

    service.login('crafter', 'pass').subscribe();
    const req = httpMock.expectOne('/login/');
    req.flush(mockResponse);

    expect(service.isAdmin()).toBe(false);
  });

  it('logout clears all stored data', () => {
    const mockResponse: LoginResponse = {
      code: '200',
      message: 'TOKEN GENERATED',
      response: { id: 1, token: 'dG9rZW46cGFzc3dvcmQ=', role: 'ADMIN', username: 'admin' },
    };

    service.login('admin', 'pass').subscribe();
    httpMock.expectOne('/login/').flush(mockResponse);

    service.logout();

    expect(service.getToken()).toBe(null);
    expect(service.getRole()).toBe(null);
    expect(service.getUsername()).toBe(null);
    expect(service.getUserId()).toBe(null);
    expect(service.isLoggedIn()).toBe(false);
  });

  it('reads saved data from localStorage on construction', () => {
    localStorage.setItem('auth_token', 'saved-token');
    localStorage.setItem('user_role', 'GARDENER');
    localStorage.setItem('user_name', 'gardener');
    localStorage.setItem('user_id', '3');

    const freshService = TestBed.runInInjectionContext(() => new AuthService(null as any));
    expect(freshService.getToken()).toBe('saved-token');
    expect(freshService.getRole()).toBe('GARDENER');
    expect(freshService.getUsername()).toBe('gardener');
    expect(freshService.getUserId()).toBe(3);
    expect(freshService.isLoggedIn()).toBe(true);
  });

  it('updateStoredUsername updates the username', () => {
    localStorage.setItem('auth_token', 't');
    localStorage.setItem('user_role', 'ADMIN');
    localStorage.setItem('user_name', 'old');
    localStorage.setItem('user_id', '1');

    const srv = TestBed.runInInjectionContext(() => new AuthService(null as any));
    srv.updateStoredUsername('new-name');

    expect(srv.getUsername()).toBe('new-name');
  });
});
