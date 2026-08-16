import { Routes } from '@angular/router';
import { RegisterComponent } from './components/register/register';
import { LoginComponent } from './components/login/login';
import { ProductListComponent } from './components/product-list/product-list';
import { ProfileComponent } from './components/profile/profile';

export const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' },
  
    // Definisanje ruta za komponente
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'product-list', component: ProductListComponent },
    { path: 'profile', component: ProfileComponent},
    
    // Ako neko ukuca pogrešnu rutu, vraća ga na login (opcionalno)
    { path: '**', redirectTo: 'login' }
];
