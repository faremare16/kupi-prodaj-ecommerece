import { Routes } from '@angular/router';
import { RegisterComponent } from './components/register/register';
import { LoginComponent } from './components/login/login';

export const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' },
  
    // Definisanje ruta za komponente
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    
    // Ako neko ukuca pogrešnu rutu, vraća ga na login (opcionalno)
    { path: '**', redirectTo: 'login' }
];
