import { HttpClient } from '@angular/common/http';
import { Injectable, Service } from '@angular/core';
import { setThrowInvalidWriteToSignalError } from '@angular/core/primitives/signals';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private baseUrl='http://localhost:8080/api/auth';

    constructor(private http: HttpClient){}

    // poziv za registraciju
    register(userData: any): Observable<any>{
        return this.http.post(`${this.baseUrl}/register`, userData);
    }

    // poziv za prijavu
    login(credentials: {email: string, password: string }): Observable<any>{
        return this.http.post(`${this.baseUrl}/login`, credentials);
    }
}
