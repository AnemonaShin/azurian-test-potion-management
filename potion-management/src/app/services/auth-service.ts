import { Injectable, signal } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { toObservable } from '@angular/core/rxjs-interop';
import { tap } from 'rxjs';

export interface LoginResponse {
  code: string;
  message: string;
  response: {
    id: number;
    token: string;
    role: string;
    username: string;
  };
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly tokenKey = 'auth_token';
  private readonly roleKey = 'user_role';
  private readonly usernameKey = 'user_name';
  private readonly userIdKey = 'user_id';

  private readonly loggedIn = signal(this.loadToken() !== null);
  private readonly role = signal<string | null>(this.loadRole());
  private readonly username = signal<string | null>(this.loadUsername());
  private readonly userId = signal<number | null>(this.loadUserId());

  readonly loggedIn$ = toObservable(this.loggedIn);
  readonly role$ = toObservable(this.role);
  readonly username$ = toObservable(this.username);
  readonly userId$ = toObservable(this.userId);

  constructor(private http: HttpClient) {}

  login(username: string, password: string) {
    const credentials = btoa(`${username}:${password}`);
    const headers = new HttpHeaders().set('Authorization', `Basic ${credentials}`);

    return this.http.get<LoginResponse>('/login/', { headers }).pipe(
      tap((res) => {
        this.storeId(res.response.id);
        this.storeToken(res.response.token);
        this.storeRole(res.response.role);
        this.storeUsername(res.response.username);
      }),
    );
  }

  getToken(): string | null {
    return this.loadToken();
  }

  getRole(): string | null {
    return this.role();
  }

  getUsername(): string | null {
    return this.username();
  }

  getUserId(): number | null {
    return this.userId();
  }

  isAdmin(): boolean {
    return this.role() === 'ADMIN';
  }

  isLoggedIn(): boolean {
    return this.loggedIn();
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.roleKey);
    localStorage.removeItem(this.usernameKey);
    localStorage.removeItem(this.userIdKey);
    this.loggedIn.set(false);
    this.role.set(null);
    this.username.set(null);
    this.userId.set(null);
  }

  private storeId(id: number) {
    localStorage.setItem(this.userIdKey, String(id));
    this.userId.set(id);
  }

  private storeToken(token: string) {
    localStorage.setItem(this.tokenKey, token);
    this.loggedIn.set(true);
  }

  private storeRole(role: string) {
    localStorage.setItem(this.roleKey, role);
    this.role.set(role);
  }

  private storeUsername(username: string) {
    localStorage.setItem(this.usernameKey, username);
    this.username.set(username);
  }

  private loadToken(): string | null {
    if (typeof window !== 'undefined') {
      return localStorage.getItem(this.tokenKey);
    }
    return null;
  }

  private loadRole(): string | null {
    if (typeof window !== 'undefined') {
      return localStorage.getItem(this.roleKey);
    }
    return null;
  }

  private loadUsername(): string | null {
    if (typeof window !== 'undefined') {
      return localStorage.getItem(this.usernameKey);
    }
    return null;
  }

  private loadUserId(): number | null {
    if (typeof window !== 'undefined') {
      const val = localStorage.getItem(this.userIdKey);
      return val ? Number(val) : null;
    }
    return null;
  }

  updateStoredUsername(username: string) {
    this.storeUsername(username);
  }
}
