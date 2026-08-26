import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "../../environments/environment";

@Injectable({
    providedIn:'root'
})

export class CategoryService {
    private apiUrl=`${environment.apiUrl}/categories`;

    constructor(private http: HttpClient){}

    getCategories(): Observable<any[]>{
        return this.http.get<any[]>(this.apiUrl);
    }
}
