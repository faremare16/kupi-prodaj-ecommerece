import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/authservice';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class NavbarComponent implements OnInit{
  dropdownOpen=false;
  userProfileImage: string  | null=null;

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

    this.http.get<any>('http://127.0.0.1:8080/api/v1/users/me',{ headers }).subscribe({
      next: (data) => {
        if (data && data.profileImageUrl) {
          
          // ako putanja nema http na početku dodaj backend URL
          this.userProfileImage = data.profileImageUrl.startsWith('http')
            ? data.profileImageUrl
            : 'http://127.0.0.1:8080' + data.profileImageUrl;
        }
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log('Error while loading user info in navbar.', err);
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
