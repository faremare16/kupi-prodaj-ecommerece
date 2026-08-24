import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn:'root' // čini ovaj servis dostupan u cijeloj aplikaciji
})
   

export class UserService {
    // osnovni url endpoint na backend-u
    private apiUrl=`${environment.apiUrl}/users`;

    // pravimo http da bi smo mogli slati HTTP zahtjeve prema serveru
    constructor(private http: HttpClient){}

    getCurrentUserInfo(): Observable<User>{
        return this.http.get<User>(`${this.apiUrl}/me`)
    }

    updateUser(user: User): Observable<User>{
        return this.http.put<User>(`${this.apiUrl}/update`, user)
    }
}