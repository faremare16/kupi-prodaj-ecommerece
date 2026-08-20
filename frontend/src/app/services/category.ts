import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable({
    providedIn:'root'
})

export class CategoryService {
    private apiUrl = window.location.hostname === 'localhost'
        ? 'http://localhost:8080/api/v1/categories'
        : 'https://kupi-prodaj-ecommercece.onrender.com/api/v1/categories';

    constructor(private http: HttpClient){}

    getCategories(): Observable<any[]>{
        const token=localStorage.getItem('authToken');
        const headers=new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });
        return this.http.get<any[]>(this.apiUrl, { headers });
    }
}
