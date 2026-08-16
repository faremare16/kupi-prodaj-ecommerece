import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable({
    providedIn:'root'
})
    
export class ProductService {
    private apiUrl='http://localhost:8080/api/v1/products';

    constructor(private http: HttpClient) { }

    getProducts(): Observable<any[]>{
        return this.http.get<any[]>(this.apiUrl);
    }

    searchProducts(query: string):Observable<any[]>{
        const token = localStorage.getItem('authToken'); 
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });

        return this.http.get<any[]>(`${this.apiUrl}/search/name?name=${query}`, { headers });
    }

    getProductByCategory(categoryId: number):Observable<any[]>{
        const token = localStorage.getItem('authToken'); 
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });

        return this.http.get<any[]>(`${this.apiUrl}/search/category?categoryId=${categoryId}`, { headers });
    }
}

