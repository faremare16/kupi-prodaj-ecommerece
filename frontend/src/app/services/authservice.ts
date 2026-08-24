import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private apiUrl=`${environment.apiUrl}/auth`;

    constructor(private http: HttpClient){}

    jwtHelper = new JwtHelperService();

    // poziv za registraciju
    register(userData: any): Observable<any>{
        return this.http.post(`${this.apiUrl}/register`, userData);
    }

    // poziv za prijavu
    login(credentials: {email: string, password: string }): Observable<any>{
        return this.http.post(`${this.apiUrl}/login`, credentials);
    }

    isAdmin(): boolean{
        const token=localStorage.getItem('authToken');
        if(!token) return false;

        const decodedToken=this.jwtHelper.decodeToken(token);
        const roles=decodedToken.roles;

        return roles && roles.includes('ROLE_ADMIN');
    }
}
