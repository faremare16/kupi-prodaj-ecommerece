import { Routes } from '@angular/router';
import { RegisterComponent } from './components/register/register';
import { LoginComponent } from './components/login/login';
import { ProductListComponent } from './components/product-list/product-list';
import { ProfileComponent } from './components/profile/profile';
import { AddItemComponent } from './components/add-item/add-item';
import { MyProductListComponent } from './components/my-product-list/my-product-list';
import { adminGuard } from './guards/admin-guard';
import { AdminComponent } from './components/admin/admin';

export const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' },
  
    // Definisanje ruta za komponente
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'product-list', component: ProductListComponent },
    { path: 'profile', component: ProfileComponent},
    { path: 'add-item', component: AddItemComponent},
    { path: 'my-product-list', component: MyProductListComponent},

    // admin guard
    {
        path: 'admin',
        component: AdminComponent,
        canActivate: [adminGuard]
    },
    
    // Ako neko ukuca pogrešnu rutu, vraća ga na login (opcionalno)
    { path: '**', redirectTo: 'login' }
];
