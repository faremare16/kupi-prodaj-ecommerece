import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable} from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user';
import { __param } from 'tslib';

@Injectable({
    providedIn:'root'
})

export class AdminService {
    private apiUrl = window.location.hostname === 'localhost'
        ? 'http://localhost:8080/api/v1/auth'
        : 'https://kupi-prodaj-ecommerece.onrender.com/api/v1/auth'


    constructor(private http: HttpClient){}

    getUsers(): Observable<User[]> {
        return this.http.get<User[]>(`${this.apiUrl}/users`);
    }   

    deleteUser(id: number): Observable<User> {
        return this.http.delete(`${this.apiUrl}/users/${id}`);

        
    }
    
}
