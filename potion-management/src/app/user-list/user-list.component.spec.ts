import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { UserListComponent } from './user-list.component';
import { AuthService } from '../services/auth-service';

function makeUser(id: number, username: string, email: string, roleName: string, roleIcon: string, active: boolean) {
  return { id, username, email, role: { id, name: roleName, roleIcon }, active };
}

describe('UserListComponent', () => {
  let fixture: ComponentFixture<UserListComponent>;
  let component: UserListComponent;
  let httpMock: HttpTestingController;
  let auth: any;

  const mockUsers = [
    makeUser(1, 'admin', 'admin@test.com', 'ADMIN', 'bi-shield-fill', true),
    makeUser(2, 'crafter', 'crafter@test.com', 'CRAFTER', 'bi-hammer', true),
    makeUser(3, 'gardener', 'gardener@test.com', 'GARDENER', 'bi-flower1', true),
    makeUser(4, 'inactive', 'off@test.com', 'GARDENER', 'bi-flower1', false),
  ];

  beforeEach(async () => {
    auth = {
      getToken: () => 'test-token',
      getUserId: () => 1,
      isAdmin: () => false,
    };

    TestBed.resetTestingModule();
    await TestBed.configureTestingModule({
      imports: [UserListComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: AuthService, useValue: auth },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(UserListComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  function flushUsers() {
    httpMock.expectOne('/users/').flush({ content: mockUsers });
    fixture.detectChanges();
  }

  it('creates the component', () => {
    expect(component).toBeTruthy();
  });

  it('shows loading state initially', () => {
    fixture.detectChanges();
    const msg = fixture.nativeElement.querySelector('.msg');
    expect(msg.textContent).toContain('Loading');
    httpMock.expectOne('/users/').flush({ content: [] });
  });

  it('loads users on init', () => {
    fixture.detectChanges();
    flushUsers();
    expect(component.loading).toBe(false);
    expect(component.allUsers.length).toBe(4);
  });

  it('shows error on failure', () => {
    fixture.detectChanges();
    const req = httpMock.expectOne('/users/');
    req.flush({}, { status: 500, statusText: 'Server Error' });
    fixture.detectChanges();

    const msg = fixture.nativeElement.querySelector('.msg.error');
    expect(msg.textContent).toContain('Failed to load users');
  });

  it('admin sees all users', () => {
    auth.isAdmin = () => true;
    fixture.detectChanges();
    flushUsers();
    expect(component.users.length).toBe(4);
  });

  it('non-admin sees only active users', () => {
    auth.isAdmin = () => false;
    fixture.detectChanges();
    flushUsers();
    expect(component.users.length).toBe(3);
  });

  it('admin sees action column', () => {
    auth.isAdmin = () => true;
    fixture.detectChanges();
    flushUsers();

    const headers = fixture.nativeElement.querySelectorAll('th');
    const actionHeader = Array.from(headers).find((h: any) => h.textContent?.trim() === 'Actions');
    expect(actionHeader).toBeTruthy();
  });

  it('non-admin hides action column', () => {
    auth.isAdmin = () => false;
    fixture.detectChanges();
    flushUsers();

    const headers = fixture.nativeElement.querySelectorAll('th');
    const actionHeader = Array.from(headers).find((h: any) => h.textContent?.trim() === 'Actions');
    expect(actionHeader).toBeFalsy();
  });

  it('renders sort indicators in table headers', () => {
    auth.isAdmin = () => true;
    fixture.detectChanges();
    flushUsers();

    const usernameHeader = fixture.nativeElement.querySelector('th.sortable');
    expect(usernameHeader.textContent).toContain('▲');
  });

  it('renders user rows with role icon', () => {
    auth.isAdmin = () => true;
    fixture.detectChanges();
    flushUsers();

    const rows = fixture.nativeElement.querySelectorAll('tbody tr');
    expect(rows.length).toBe(4);
    const icon = rows[0].querySelector('i');
    expect(icon.className).toContain('bi-shield-fill');
    expect(rows[0].textContent).toContain('ADMIN');
  });

  it('shows inactive row styling', () => {
    auth.isAdmin = () => true;
    fixture.detectChanges();
    flushUsers();

    const rows = fixture.nativeElement.querySelectorAll('tbody tr');
    expect(rows[3].className).toContain('inactive');
    expect(rows[3].textContent).toContain('Inactive');
  });

  it('admin sees action buttons for other users but not self', () => {
    auth.isAdmin = () => true;
    fixture.detectChanges();
    flushUsers();

    const rows = fixture.nativeElement.querySelectorAll('tbody tr');
    const selfActions = rows[0].querySelector('.actions');
    expect(selfActions.textContent).not.toContain('Deactivate');
    expect(selfActions.textContent).not.toContain('Update');

    const otherActions = rows[1].querySelector('.actions');
    expect(otherActions.textContent).toContain('Update');
    expect(otherActions.textContent).toContain('Deactivate');
  });

  it('filters users by search term', () => {
    auth.isAdmin = () => true;
    fixture.detectChanges();
    httpMock.expectOne('/users/').flush({ content: mockUsers });
    component.searchTerm = 'admin';
    fixture.detectChanges();

    expect(component.filteredUsers.length).toBe(1);
    expect(component.filteredUsers[0].username).toBe('admin');
  });

  it('filters users by email search', () => {
    auth.isAdmin = () => true;
    fixture.detectChanges();
    httpMock.expectOne('/users/').flush({ content: mockUsers });
    component.searchTerm = 'crafter@test';
    fixture.detectChanges();

    expect(component.filteredUsers.length).toBe(1);
    expect(component.filteredUsers[0].username).toBe('crafter');
  });

  it('toggles sort field', () => {
    fixture.detectChanges();
    flushUsers();

    component.toggleSort('email');
    expect(component.sortField).toBe('email');
    expect(component.sortDir).toBe('asc');
  });

  it('toggles sort direction for same field', () => {
    fixture.detectChanges();
    flushUsers();

    component.toggleSort('username');
    expect(component.sortField).toBe('username');
    expect(component.sortDir).toBe('desc');
  });

  it('isSelf returns true for current user', () => {
    fixture.detectChanges();
    flushUsers();
    expect(component.isSelf(mockUsers[0])).toBe(true);
  });

  it('isSelf returns false for other users', () => {
    fixture.detectChanges();
    flushUsers();
    expect(component.isSelf(mockUsers[1])).toBe(false);
  });

  it('isSelf returns false for null', () => {
    expect(component.isSelf(null)).toBe(false);
  });

  it('openUpdateModal sets form data', () => {
    fixture.detectChanges();
    flushUsers();

    component.openUpdateModal(mockUsers[1]);
    expect(component.showModal).toBe(true);
    expect(component.form.username).toBe('crafter');
    expect(component.form.email).toBe('crafter@test.com');
    expect(component.form.role).toBe('CRAFTER');
  });

  it('renders modal with self-restriction hint for own account', () => {
    auth.isAdmin = () => true;
    fixture.detectChanges();
    flushUsers();

    component.openUpdateModal(mockUsers[0]);
    fixture.detectChanges();

    const modal = fixture.nativeElement.querySelector('.modal-overlay');
    expect(modal).toBeTruthy();
    expect(modal.textContent).toContain('You cannot change your own role');
  });

  it('renders modal with role dropdown for other users', () => {
    auth.isAdmin = () => true;
    fixture.detectChanges();
    flushUsers();

    component.openUpdateModal(mockUsers[1]);
    fixture.detectChanges();

    const modal = fixture.nativeElement.querySelector('.modal-overlay');
    expect(modal).toBeTruthy();
    expect(modal.textContent).not.toContain('You cannot change your own role');
  });

  it('closeModal hides the modal', () => {
    fixture.detectChanges();
    flushUsers();

    component.openUpdateModal(mockUsers[1]);
    component.closeModal();
    expect(component.showModal).toBe(false);
  });

  it('saveUser calls PUT endpoint and reloads', () => {
    fixture.detectChanges();
    flushUsers();

    component.openUpdateModal(mockUsers[1]);
    component.form.username = 'updated';
    component.saveUser();

    const req = httpMock.expectOne('/users/2');
    expect(req.request.method).toBe('PUT');
    expect(req.request.body.username).toBe('updated');
    req.flush({});

    httpMock.expectOne('/users/').flush({ content: [] });
  });

  it('deactivateUser calls DELETE endpoint and reloads', () => {
    fixture.detectChanges();
    flushUsers();

    component.deactivateUser(mockUsers[1]);
    const req = httpMock.expectOne('/users/2');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);

    httpMock.expectOne('/users/').flush({ content: [] });
  });

  it('activateUser calls PATCH endpoint and reloads', () => {
    fixture.detectChanges();
    flushUsers();

    component.activateUser(mockUsers[3]);
    const req = httpMock.expectOne('/users/4/activate');
    expect(req.request.method).toBe('PATCH');
    req.flush(null);

    httpMock.expectOne('/users/').flush({ content: [] });
  });

  it('sortIndicator returns arrow for active sort field', () => {
    fixture.detectChanges();
    flushUsers();

    component.sortField = 'email';
    component.sortDir = 'desc';
    expect(component.sortIndicator('email')).toBe('\u25BC');
    expect(component.sortIndicator('username')).toBe('');
  });
});
