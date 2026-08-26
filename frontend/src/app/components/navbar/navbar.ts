import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/authservice';
import { User } from '../../models/user';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class NavbarComponent implements OnInit{
  user: User | null = null;
  dropdownOpen=false;
  userProfileImage: string  | null=null;
  isLoading: boolean=true;
  editData: Partial<User>={};
  isAdmin: boolean=false;

  constructor(private router: Router, 
              private cdr: ChangeDetectorRef,
              private http: HttpClient,
              public authService: AuthService){}

  ngOnInit(): void {
    this.loadUserProfile();
  }

  loadUserProfile(){
    this.http.get<any>(`${environment.apiUrl}/users/me`, { withCredentials: true }).subscribe({
      next: (data)=>{
        this.user = data as User; 
        this.editData = { ...data } as Partial<User>;

        // Provjeravamo da li korisnik ima ulogu admina
        const roles = data.roles || [];
        this.isAdmin = roles.some((role: any) => 
          typeof role === 'string' ? role === 'ROLE_ADMIN' : role.name === 'ROLE_ADMIN'
        );

        if (data && data.profileImageUrl) {
          this.userProfileImage = data.profileImageUrl.startsWith('http')
            ? data.profileImageUrl
            : environment.apiUrl.replace('/api/v1', '') + data.profileImageUrl;
        }

        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err)=>{
        console.log('Korisnik nije prijavljen ili je sesija istekla.', err);
        this.user = null;
        this.isAdmin = false;
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }


  isLoggedIn(): boolean{
    return this.user!==null;
  }

  toggleDropdown(){
    this.dropdownOpen=!this.dropdownOpen;
  }

  getUserInitials(): string{
    return 'FH';
  }

  logout() {
  this.http.post(`${environment.apiUrl}/auth/logout`, {}, { withCredentials: true })
    .subscribe({
      next: () => {
        this.dropdownOpen = false; 
        
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Logout error', err);
        this.router.navigate(['/login']);
      }
    });
  }
}
