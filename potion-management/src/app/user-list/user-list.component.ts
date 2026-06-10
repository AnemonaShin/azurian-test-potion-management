import { Component, inject, ChangeDetectorRef, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { NgFor, NgIf } from '@angular/common';
import { AuthService } from '../services/auth-service';
import { roleIconClass } from '../utils/role-icons';

interface UserResponse {
  id: number;
  username: string;
  email: string;
  role: { id: number; name: string; roleIcon: string };
  active: boolean;
}

interface UserRequest {
  username: string;
  password: string;
  email: string;
  role: string;
}

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [NgFor, NgIf, FormsModule],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css',
})
export class UserListComponent implements OnInit {
  private http = inject(HttpClient);
  private auth = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);

  allUsers: UserResponse[] = [];
  loading = true;
  error = '';

  searchTerm = '';
  sortField: 'username' | 'email' = 'username';
  sortDir: 'asc' | 'desc' = 'asc';

  showModal = false;
  editingUser: UserResponse | null = null;
  form: UserRequest = { username: '', password: '', email: '', role: 'CRAFTER' };
  saving = false;
  modalError = '';

  roleOptions = ['ADMIN', 'CRAFTER', 'GARDENER'];

  get isAdmin(): boolean {
    return this.auth.isAdmin();
  }

  get currentUserId(): number | null {
    return this.auth.getUserId();
  }

  get users(): UserResponse[] {
    if (this.isAdmin) return this.allUsers;
    return this.allUsers.filter((u) => u.active);
  }

  get filteredUsers(): UserResponse[] {
    let list = this.users;

    if (this.searchTerm.trim()) {
      const q = this.searchTerm.trim().toLowerCase();
      list = list.filter(
        (u) =>
          u.username.toLowerCase().includes(q) ||
          u.email.toLowerCase().includes(q),
      );
    }

    const dir = this.sortDir === 'asc' ? 1 : -1;
    list = [...list].sort((a, b) => {
      const valA = (a[this.sortField] ?? '').toLowerCase();
      const valB = (b[this.sortField] ?? '').toLowerCase();
      return valA < valB ? -dir : valA > valB ? dir : 0;
    });

    return list;
  }

  isSelf(user: UserResponse | null): boolean {
    return user !== null && this.currentUserId !== null && user.id === this.currentUserId;
  }

  iconClass(icon: string | undefined | null): string {
    return roleIconClass(icon);
  }

  toggleSort(field: 'username' | 'email') {
    if (this.sortField === field) {
      this.sortDir = this.sortDir === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDir = 'asc';
    }
  }

  sortIndicator(field: 'username' | 'email'): string {
    if (this.sortField !== field) return '';
    return this.sortDir === 'asc' ? '\u25B2' : '\u25BC';
  }

  ngOnInit() {
    this.loadUsers();
  }

  private loadUsers() {
    const token = this.auth.getToken();
    const headers: Record<string, string> = {};
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    this.http
      .get<{ content: UserResponse[] }>('/users/', { headers })
      .subscribe({
        next: (res) => {
          this.allUsers = res.content ?? [];
          this.loading = false;
          this.cdr.markForCheck();
        },
        error: (err) => {
          console.error('Users error:', err);
          this.error = 'Failed to load users';
          this.loading = false;
          this.cdr.markForCheck();
        },
      });
  }

  openUpdateModal(user: UserResponse) {
    this.editingUser = user;
    this.form = {
      username: user.username,
      password: '',
      email: user.email,
      role: user.role.name,
    };
    this.modalError = '';
    this.showModal = true;
    this.cdr.markForCheck();
  }

  closeModal() {
    this.showModal = false;
    this.editingUser = null;
    this.cdr.markForCheck();
  }

  saveUser() {
    if (!this.editingUser) return;
    this.saving = true;
    this.modalError = '';

    const token = this.auth.getToken();
    const headers: Record<string, string> = {};
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    this.http
      .put(`/users/${this.editingUser.id}`, this.form, { headers })
      .subscribe({
        next: () => {
          this.saving = false;
          this.closeModal();
          this.loadUsers();
        },
        error: (err) => {
          this.saving = false;
          this.modalError = 'Update failed';
          console.error('Update error:', err);
          this.cdr.markForCheck();
        },
      });
  }

  deactivateUser(user: UserResponse) {
    const token = this.auth.getToken();
    const headers: Record<string, string> = {};
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    this.http.delete(`/users/${user.id}`, { headers }).subscribe({
      next: () => this.loadUsers(),
      error: (err) => {
        console.error('Deactivate error:', err);
        this.error = 'Failed to deactivate user';
        this.cdr.markForCheck();
      },
    });
  }

  activateUser(user: UserResponse) {
    const token = this.auth.getToken();
    const headers: Record<string, string> = {};
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    this.http.patch(`/users/${user.id}/activate`, null, { headers }).subscribe({
      next: () => this.loadUsers(),
      error: (err) => {
        console.error('Activate error:', err);
        this.error = 'Failed to activate user';
        this.cdr.markForCheck();
      },
    });
  }
}
