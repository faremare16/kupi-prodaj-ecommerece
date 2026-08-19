import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Product } from "../models/product";

@Injectable({
    providedIn:'root'
})
    
export class ProductService {
    private apiUrl='http://localhost:8080/api/v1/products';

    constructor(private http: HttpClient) { }

    getProducts(): Observable<any[]>{
        return this.http.get<any[]>(this.apiUrl);
    }

    searchProducts(query: string):Observable<Product[]>{
        const token = localStorage.getItem('authToken'); 
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });

        return this.http.get<Product[]>(`${this.apiUrl}/search/name?name=${query}`, { headers });
    }

    getProductByCategory(categoryId: number):Observable<Product[]>{
        const token = localStorage.getItem('authToken'); 
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });

        return this.http.get<Product[]>(`${this.apiUrl}/search/category?categoryId=${categoryId}`, { headers });
    }

    getProductById(id: number):Observable<Product>{
        const token = localStorage.getItem('authToken'); 
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });

        return this.http.get<Product>(`${this.apiUrl}/${id}`, { headers });
    }

    getProductByUserId(userId: number):Observable<Product[]>{
        const token = localStorage.getItem('authToken'); 
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });

        return this.http.get<Product[]>(`${this.apiUrl}/user/${userId}`, { headers });
    }

    createProduct(formData: FormData): Observable<Product> {
        const token = localStorage.getItem('authToken');
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });

        return this.http.post<Product>(this.apiUrl, formData, { headers });
    }

    deleteProduct(id: number): Observable<any> {
        const token = localStorage.getItem('authToken'); 
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });

        return this.http.delete(`${this.apiUrl}/${id}`, { headers });
    }
}

