import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/authservice';
import { Router } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';
import { finalize } from 'rxjs';


@Component({
  selector: 'app-register',
  imports: [CommonModule, FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class RegisterComponent {
  registerData={ username: '', email: '', password: '' };
  successMessage='';
  errorMessage='';

  constructor(private authService: AuthService, private router: Router, private cdr: ChangeDetectorRef){}

  onRegister(){
    this.errorMessage = '';
    this.successMessage = '';
    this.cdr.detectChanges();

      this.authService.register(this.registerData).pipe(
        finalize(() => {
          this.cdr.detectChanges();
        })
      ).subscribe({
        next: (response) => {
          this.successMessage = 'Registration sucesfull redirecting to login...';
          this.cdr.detectChanges();
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 2000);
        },
        error: (err) => {
          if (err.error && typeof err.error === 'string') {
            this.errorMessage = err.error;
          } else if (err.error && err.error.message) {
            this.errorMessage = err.error.message;
          } else {
            this.errorMessage = 'Error, username or email already exist';
          }
            this.cdr.detectChanges();
        }
      });
    }
  }
