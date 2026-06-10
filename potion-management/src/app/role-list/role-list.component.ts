import { Component, inject, ChangeDetectorRef, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { NgFor, NgIf } from '@angular/common';
import { AuthService } from '../services/auth-service';
import { roleIconClass } from '../utils/role-icons';

interface RoleResponse {
  id: number;
  name: string;
  roleIcon: string;
  active: boolean;
}

interface RoleRequest {
  name: string;
  roleIcon: string;
}

@Component({
  selector: 'app-role-list',
  standalone: true,
  imports: [NgFor, NgIf, FormsModule],
  templateUrl: './role-list.component.html',
  styleUrl: './role-list.component.css',
})
export class RoleListComponent implements OnInit {
  private http = inject(HttpClient);
  private auth = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);

  allRoles: RoleResponse[] = [];
  loading = true;
  error = '';

  showModal = false;
  editingRole: RoleResponse | null = null;
  form: RoleRequest = { name: '', roleIcon: '' };
  saving = false;
  modalError = '';

  get isAdmin(): boolean {
    return this.auth.isAdmin();
  }

  get roles(): RoleResponse[] {
    if (this.isAdmin) return this.allRoles;
    return this.allRoles.filter((r) => r.active);
  }

  iconClass(icon: string | undefined | null): string {
    return roleIconClass(icon);
  }

  ngOnInit() {
    this.loadRoles();
  }

  private loadRoles() {
    const token = this.auth.getToken();
    const headers: Record<string, string> = {};
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    this.http
      .get<{ content: RoleResponse[] }>('/roles/', {
        params: { size: '10', page: '0' },
        headers,
      })
      .subscribe({
        next: (res) => {
          this.allRoles = res.content ?? [];
          this.loading = false;
          this.cdr.markForCheck();
        },
        error: (err) => {
          console.error('Roles error:', err);
          this.error = 'Failed to load roles';
          this.loading = false;
          this.cdr.markForCheck();
        },
      });
  }

  openUpdateModal(role: RoleResponse) {
    this.editingRole = role;
    this.form = {
      name: role.name,
      roleIcon: role.roleIcon.replace(/^bi-/, ''),
    };
    this.modalError = '';
    this.showModal = true;
    this.cdr.markForCheck();
  }

  closeModal() {
    this.showModal = false;
    this.editingRole = null;
    this.cdr.markForCheck();
  }

  saveRole() {
    if (!this.editingRole) return;
    this.saving = true;
    this.modalError = '';

    const token = this.auth.getToken();
    const headers: Record<string, string> = {};
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    this.http
      .put(`/roles/${this.editingRole.id}`, this.form, { headers })
      .subscribe({
        next: () => {
          this.saving = false;
          this.closeModal();
          this.loadRoles();
        },
        error: (err) => {
          this.saving = false;
          this.modalError = 'Update failed';
          console.error('Update error:', err);
          this.cdr.markForCheck();
        },
      });
  }

  deactivateRole(role: RoleResponse) {
    const token = this.auth.getToken();
    const headers: Record<string, string> = {};
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    this.http.delete(`/roles/${role.id}`, { headers }).subscribe({
      next: () => this.loadRoles(),
      error: (err) => {
        console.error('Deactivate error:', err);
        this.error = 'Failed to deactivate role';
        this.cdr.markForCheck();
      },
    });
  }

  activateRole(role: RoleResponse) {
    const token = this.auth.getToken();
    const headers: Record<string, string> = {};
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    this.http.patch(`/roles/${role.id}/activate`, null, { headers }).subscribe({
      next: () => this.loadRoles(),
      error: (err) => {
        console.error('Activate error:', err);
        this.error = 'Failed to activate role';
        this.cdr.markForCheck();
      },
    });
  }
}
