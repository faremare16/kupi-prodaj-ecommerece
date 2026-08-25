import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
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

  constructor(private router: Router, 
              private cdr: ChangeDetectorRef,
              private http: HttpClient,
              public authService: AuthService){}

  ngOnInit(): void {
    if(this.isLoggedIn()){
      this.loadUserProfile();
    }
  }

  loadUserProfile(){
    const token=localStorage.getItem('authToken');
    const headers=new HttpHeaders({ 'Authorization': `Bearer ${token}`});

    this.http.get<any>(`${environment.apiUrl}/users/me`,{ headers }).subscribe({
      next: (data)=>{
        this.user=data as User; 
        this.editData={ ...data } as Partial<User>;

        // OVO TI JE NEDOSTAJALO:
        if (data && data.profileImageUrl) {
          this.userProfileImage = data.profileImageUrl.startsWith('http')
            ? data.profileImageUrl
            : environment.apiUrl.replace('/api/v1', '') + data.profileImageUrl;
        }

        this.isLoading=false;
        this.cdr.detectChanges();
      },
      error: (err)=>{
        console.log('Error while loading user info.', err);
        this.isLoading=false;
        this.cdr.detectChanges();
      }
    });
  }


  isLoggedIn(): boolean{
    const token = localStorage.getItem('authToken');
    return !!token && token !== 'undefined' && token !== 'null';
  }

  toggleDropdown(){
    this.dropdownOpen=!this.dropdownOpen;
  }

  getUserInitials(): string{
    return 'FH';
  }

  logout(){
    localStorage.removeItem('authToken');
    this.dropdownOpen=false;
    this.router.navigate(['/login']);
    this.cdr.detectChanges
  }
}
