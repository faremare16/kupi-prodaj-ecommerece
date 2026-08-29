import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private apiUrl=`${environment.apiUrl}/auth`;
    private userUrl=`${environment.apiUrl}/users`;

    constructor(private http: HttpClient){}


    // poziv za registraciju
    register(userData: any): Observable<any>{
        return this.http.post(`${this.apiUrl}/register`, userData, {
            responseType: 'text',
            withCredentials: true
        });
    }

    // poziv za prijavu
    login(credentials: {email: string, password: string }): Observable<any>{
        return this.http.post(`${this.apiUrl}/login`, credentials, {
            withCredentials: true,
            responseType: 'text'
        });
    }

    // dobijemo podatke trenutnog korisnika ukljucujuci role
    getCurrentUser(): Observable<any> {
        return this.http.get(`${this.userUrl}/me`, {
            withCredentials: true
        });
    }

    isAdmin(): Observable<boolean> {
    return this.getCurrentUser().pipe(
        map((user: any) => {
        const roles = user.roles || [];
        return roles.includes('ROLE_ADMIN');
        }),
        catchError(() => of(false)) 
    );
    }
}
