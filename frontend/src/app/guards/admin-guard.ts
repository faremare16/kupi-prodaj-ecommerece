import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/authservice';
import { inject } from '@angular/core';

export const adminGuard: CanActivateFn = (route, state) => {
  const authService=inject(AuthService);
  const router=inject(Router);

  if(authService.isAdmin()){
    return true;
  }else{
    router.navigate(['product-list']);
    return false;
  }
};
