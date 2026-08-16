import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user';

@Injectable({
    providedIn:'root'
})
   

export class UserService {
    private apiUrl='http://localhost:8080/api/v1/users'

    constructor(private http: HttpClient){}

    getCurrentUserInfo(): Observable<User>{
        return this.http.get<User>(`${this.apiUrl}/me`)
    }

    updateUser(user: User): Observable<User>{
        return this.http.put<User>(`${this.apiUrl}/update`, user)
    }


}