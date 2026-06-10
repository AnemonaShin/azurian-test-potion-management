import { Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { NgIf } from '@angular/common';
import { AuthService } from '../services/auth-service';

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [NgIf, RouterModule],
  templateUrl: './nav.component.html',
  styleUrl: './nav.component.css',
})
export class NavComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  open = false;

  get isAdmin() {
    return this.authService.isAdmin();
  }

  toggle() {
    this.open = !this.open;
  }

  close() {
    this.open = false;
  }

  navigate(route: string) {
    this.close();
    this.router.navigate([route]).catch(() => {});
  }

  logout() {
    this.close();
    this.authService.logout();
    this.router.navigate(['/login']).catch(() => {});
  }
}
