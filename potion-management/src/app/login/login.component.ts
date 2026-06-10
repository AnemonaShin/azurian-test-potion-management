import { Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { NgFor, NgIf } from '@angular/common';
import { AuthService } from '../services/auth-service';
import { roleIconClass } from '../utils/role-icons';

interface RegisterRequest {
  username: string;
  password: string;
  email: string;
  role: string;
}

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, NgIf, NgFor, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  private authService = inject(AuthService);
  private http = inject(HttpClient);
  private router = inject(Router);

  showRegister = false;
  error = '';

  username = '';
  password = '';
  regUsername = '';
  regPassword = '';
  regEmail = '';
  regRole = 'GARDENER';

  roleOptions = [
    { value: 'GARDENER', icon: 'flower1', label: 'Gardener' },
    { value: 'CRAFTER', icon: 'hammer', label: 'Crafter' },
  ];

  iconClass(icon: string | undefined | null): string {
    return roleIconClass(icon);
  }

  toggleRegister() {
    this.showRegister = !this.showRegister;
    this.error = '';
  }

  login() {
    this.error = '';
    this.authService.login(this.username, this.password).subscribe({
      next: () => {
        this.router.navigate(['/home']);
      },
      error: () => {
        this.error = 'Invalid credentials';
      },
    });
  }

  register() {
    this.error = '';

    const body: RegisterRequest = {
      username: this.regUsername,
      password: this.regPassword,
      email: this.regEmail,
      role: this.regRole,
    };

    this.http.post('/users/', body).subscribe({
      next: () => {
        this.authService.login(this.regUsername, this.regPassword).subscribe({
          next: () => {
            this.router.navigate(['/home']);
          },
          error: () => {
            this.error = 'Account created but login failed. Please sign in manually.';
            this.showRegister = false;
          },
        });
      },
      error: (err) => {
        const msg = err.error?.message;
        this.error = msg ? `Registration failed: ${msg}` : 'Registration failed';
      },
    });
  }
}
