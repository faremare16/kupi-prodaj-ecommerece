import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/authservice';
import { Router } from '@angular/router';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class LoginComponent {
  loginData={ email:'', 
              password:'' 
  };
  errorMessage='';

  constructor(private authService: AuthService,private router: Router, private cdr: ChangeDetectorRef){ }

  onLogin(){
    this.authService.login(this.loginData).pipe(
      finalize(()=>{
        this.cdr.detectChanges();
      })
    ).subscribe({
      next: (response)=>{
        console.log('Login succesfull', response);
        localStorage.setItem('authToken', response.token); 
        this.router.navigate(['/product-list']);
      },
      error: (err)=>{
        if(err.error && typeof err.error==='string'){
          this.errorMessage=err.error;
        }else if(err.error && err.error.message){
          this.errorMessage=err.error.message;
        }else{
          this.errorMessage='Invalid email or password, please try again.';
        }
        this.cdr.detectChanges();
      }
    })
  }
}
