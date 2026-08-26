import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class UserService {
    private apiUrl = `${environment.apiUrl}/users`;

    constructor(private http: HttpClient) {}

    getCurrentUserInfo(): Observable<User> {
        return this.http.get<User>(`${this.apiUrl}/me`);
    }

    updateUser(formData: FormData): Observable<User> {
        return this.http.put<User>(`${this.apiUrl}/me`, formData);
        withCredentials: true
    }
}