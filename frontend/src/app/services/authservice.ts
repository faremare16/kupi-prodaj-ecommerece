import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private apiUrl='http://localhost:8080/api/v1/auth';

    constructor(private http: HttpClient){}

    // poziv za registraciju
    register(userData: any): Observable<any>{
        return this.http.post(`${this.apiUrl}/register`, userData);
    }

    // poziv za prijavu
    login(credentials: {email: string, password: string }): Observable<any>{
        return this.http.post(`${this.apiUrl}/login`, credentials);
    }
}
