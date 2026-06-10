import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { RoleListComponent } from './role-list.component';
import { AuthService } from '../services/auth-service';

function makeRole(id: number, name: string, roleIcon: string, active: boolean) {
  return { id, name, roleIcon, active };
}

describe('RoleListComponent', () => {
  let fixture: ComponentFixture<RoleListComponent>;
  let component: RoleListComponent;
  let httpMock: HttpTestingController;
  let auth: any;

  const mockRoles = [
    makeRole(1, 'ADMIN', 'bi-shield-fill', true),
    makeRole(2, 'CRAFTER', 'bi-hammer', true),
    makeRole(3, 'GARDENER', 'bi-flower1', true),
    makeRole(4, 'RETIRED', 'bi-archive', false),
  ];

  beforeEach(async () => {
    auth = {
      getToken: () => 'test-token',
      isAdmin: () => false,
    };

    TestBed.resetTestingModule();
    await TestBed.configureTestingModule({
      imports: [RoleListComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: AuthService, useValue: auth },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RoleListComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  function flushRoles() {
    httpMock.expectOne('/roles/?size=10&page=0').flush({ content: mockRoles });
    fixture.detectChanges();
  }

  it('creates the component', () => {
    expect(component).toBeTruthy();
  });

  it('shows loading state initially', () => {
    fixture.detectChanges();
    const msg = fixture.nativeElement.querySelector('.msg');
    expect(msg.textContent).toContain('Loading');
    httpMock.expectOne('/roles/?size=10&page=0').flush({ content: [] });
  });

  it('shows error on API failure', () => {
    fixture.detectChanges();
    const req = httpMock.expectOne('/roles/?size=10&page=0');
    req.flush({}, { status: 500, statusText: 'Server Error' });
    fixture.detectChanges();

    const msg = fixture.nativeElement.querySelector('.msg.error');
    expect(msg.textContent).toContain('Failed to load roles');
  });

  it('loads roles on init', () => {
    fixture.detectChanges();
    flushRoles();
    expect(component.loading).toBe(false);
    expect(component.allRoles.length).toBe(4);
  });

  it('admin sees all roles', () => {
    auth.isAdmin = () => true;
    fixture.detectChanges();
    flushRoles();
    expect(component.roles.length).toBe(4);
  });

  it('non-admin sees only active roles', () => {
    auth.isAdmin = () => false;
    fixture.detectChanges();
    flushRoles();
    expect(component.roles.length).toBe(3);
  });

  it('renders table rows with icon class', () => {
    auth.isAdmin = () => true;
    fixture.detectChanges();
    flushRoles();

    const rows = fixture.nativeElement.querySelectorAll('tbody tr');
    expect(rows.length).toBe(4);
    const icon = rows[0].querySelector('i');
    expect(icon.className).toContain('bi-shield-fill');
  });

  it('shows active and inactive badges', () => {
    auth.isAdmin = () => true;
    fixture.detectChanges();
    flushRoles();

    const badges = fixture.nativeElement.querySelectorAll('.badge');
    expect(badges.length).toBe(4);
    expect(badges[0].className).toContain('active-badge');
    expect(badges[3].className).toContain('inactive-badge');
  });

  it('admin sees deactivate/activate buttons', () => {
    auth.isAdmin = () => true;
    fixture.detectChanges();
    flushRoles();

    const buttons = fixture.nativeElement.querySelectorAll('.actions .btn');
    expect(buttons.length).toBeGreaterThan(0);
  });

  it('openUpdateModal renders modal with icon preview', () => {
    auth.isAdmin = () => true;
    fixture.detectChanges();
    flushRoles();

    component.openUpdateModal(mockRoles[1]);
    fixture.detectChanges();

    const modal = fixture.nativeElement.querySelector('.modal-overlay');
    expect(modal).toBeTruthy();
    expect(component.form.name).toBe('CRAFTER');
    const preview = modal.querySelector('.icon-preview');
    expect(preview).toBeTruthy();
    expect(preview.textContent).toContain('bi-hammer');
  });

  it('closeModal hides the modal', () => {
    fixture.detectChanges();
    flushRoles();

    component.openUpdateModal(mockRoles[1]);
    component.closeModal();
    expect(component.showModal).toBe(false);
  });

  it('saveRole calls PUT endpoint and reloads', () => {
    fixture.detectChanges();
    flushRoles();

    component.openUpdateModal(mockRoles[1]);
    component.form.name = 'UPDATED';
    component.saveRole();

    const req = httpMock.expectOne('/roles/2');
    expect(req.request.method).toBe('PUT');
    expect(req.request.body.name).toBe('UPDATED');
    req.flush({});

    httpMock.expectOne('/roles/?size=10&page=0').flush({ content: [] });
  });

  it('deactivateRole calls DELETE endpoint and reloads', () => {
    fixture.detectChanges();
    flushRoles();

    component.deactivateRole(mockRoles[1]);
    const req = httpMock.expectOne('/roles/2');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);

    httpMock.expectOne('/roles/?size=10&page=0').flush({ content: [] });
  });

  it('activateRole calls PATCH endpoint and reloads', () => {
    fixture.detectChanges();
    flushRoles();

    component.activateRole(mockRoles[3]);
    const req = httpMock.expectOne('/roles/4/activate');
    expect(req.request.method).toBe('PATCH');
    req.flush(null);

    httpMock.expectOne('/roles/?size=10&page=0').flush({ content: [] });
  });

  it('iconClass delegates to utility', () => {
    fixture.detectChanges();
    flushRoles();
    expect(component.iconClass('shield-fill')).toBe('bi-shield-fill');
  });
});

