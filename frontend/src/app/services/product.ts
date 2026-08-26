import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Product } from "../models/product";
import { environment } from "../../environments/environment";

@Injectable({
    providedIn:'root'
})
    
export class ProductService {
    private apiUrl=`${environment.apiUrl}/products`;

    constructor(private http: HttpClient) { }

    getProducts(): Observable<any[]>{
        return this.http.get<any[]>(this.apiUrl);
    }

    searchProducts(query: string):Observable<Product[]>{
        return this.http.get<Product[]>(`${this.apiUrl}/search/name?name=${query}`);
    }

    getProductByCategory(categoryId: number):Observable<Product[]>{
        return this.http.get<Product[]>(`${this.apiUrl}/search/category?categoryId=${categoryId}`);
    }

    getProductById(id: number):Observable<Product>{
        return this.http.get<Product>(`${this.apiUrl}/${id}`);
    }

    getProductByUserId(userId: number):Observable<Product[]>{
        return this.http.get<Product[]>(`${this.apiUrl}/user/${userId}`);
    }

    createProduct(formData: FormData): Observable<Product> {
        return this.http.post<Product>(this.apiUrl, formData);
        withCredentials: true
    }

    deleteProduct(id: number): Observable<any> {
        return this.http.delete(`${this.apiUrl}/${id}`);
    }
}

