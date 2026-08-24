import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable} from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user';
import { __param } from 'tslib';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn:'root'
})

export class AdminService {
    private apiUrl=`${environment.apiUrl}/users`;


    constructor(private http: HttpClient){}

    getUsers(): Observable<User[]> {
        return this.http.get<User[]>(this.apiUrl);
    }   

    deleteUser(id: number): Observable<User> {
        return this.http.delete(`${this.apiUrl}/${id}`);

        
    }
    
}
