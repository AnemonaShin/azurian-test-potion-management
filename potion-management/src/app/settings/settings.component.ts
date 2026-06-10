import { Component, inject, ChangeDetectorRef, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';
import { AuthService } from '../services/auth-service';
import { roleIconClass } from '../utils/role-icons';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [FormsModule, NgIf],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css',
})
export class SettingsComponent implements OnInit {
  private http = inject(HttpClient);
  private auth = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);

  username = '';
  email = '';
  password = '';
  role = '';
  roleIcon = '';
  loading = true;
  saving = false;
  error = '';
  success = '';

  iconClass(icon: string | undefined | null): string {
    return roleIconClass(icon);
  }

  ngOnInit() {
    const token = this.auth.getToken();
    const headers: Record<string, string> = {};
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    const storedUsername = this.auth.getUsername();
    if (!storedUsername) {
      this.error = 'Not authenticated';
      this.loading = false;
      return;
    }

    this.http
      .get<{ response: { id: number; username: string; email: string; role: { name: string; roleIcon: string } } }>(
        `/users/${storedUsername}`,
        { headers },
      )
      .subscribe({
        next: (res) => {
          this.username = res.response.username;
          this.email = res.response.email;
          this.role = res.response.role.name;
          this.roleIcon = res.response.role.roleIcon;
          this.loading = false;
          this.cdr.markForCheck();
        },
        error: () => {
          this.error = 'Failed to load profile';
          this.loading = false;
          this.cdr.markForCheck();
        },
      });
  }

  save() {
    this.error = '';
    this.success = '';
    this.saving = true;

    const token = this.auth.getToken();
    const headers: Record<string, string> = {};
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    const userId = this.auth.getUserId();
    if (!userId) {
      this.error = 'Not authenticated';
      this.saving = false;
      return;
    }

    const body: Record<string, string> = { username: this.username, email: this.email, password: this.password, role: this.role };

    this.http.put(`/users/${userId}`, body, { headers }).subscribe({
      next: () => {
        this.auth.updateStoredUsername(this.username);
        this.success = 'Profile updated successfully';
        this.password = '';
        this.saving = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        const msg = err.error?.message;
        this.error = msg ? `Update failed: ${msg}` : 'Update failed';
        this.saving = false;
        this.cdr.markForCheck();
      },
    });
  }
}
