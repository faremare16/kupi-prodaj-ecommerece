import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { RouterModule, Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class NavbarComponent {
  dropdownOpen=false;
  userProfileImage: string  | null=null;

  constructor(private router: Router, private cdr: ChangeDetectorRef){}

  isLoggedIn(): boolean{
    return !!localStorage.getItem('authToken');
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
