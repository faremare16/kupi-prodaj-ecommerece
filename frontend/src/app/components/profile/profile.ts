import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { User } from '../../models/user';
import { Product } from '../../models/product';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.html',
  styleUrls: ['./profile.css'],
})
export class ProfileComponent implements OnInit{
  user: User | null = null;
  isLoading: boolean=true;
  myProducts: Product[] = [];
  isEditing: boolean=false;

  editData: Partial<User>={};
  selectedFile: File | null=null;
  imageError: string | null=null;

  ngOnInit(): void {
    this.loadUserProfile();
  }

  constructor(private cdr: ChangeDetectorRef, 
              private http: HttpClient){}

  loadUserProfile(){
    const token=localStorage.getItem('authToken');
    const headers=new HttpHeaders({ 'Authorization': `Bearer ${token}`});

    this.http.get<User>(`${environment.apiUrl}/users/me`, { headers }).subscribe({
      next: (data)=>{
        this.user=data; 
        this.editData={ ...data } as Partial<User>;
        this.isLoading=false;
        this.cdr.detectChanges();
      },
      error: (err)=>{
        console.log('Error while loading user info.', err);
        this.isLoading=false;
        this.cdr.detectChanges();
      }
    });
  }

  toggleEdit(){
    this.isEditing=!this.isEditing;

    if(this.isEditing){
      this.editData={ ...this.user };
      this.selectedFile=null;
      this.imageError=null;
    }
  }

  onFileSelected(event: any): void{
    const file: File=event.target.files[0];
    if(file){
      this.handleFile(file);
    }
  }

  handleFile(file: File): void{
    this.imageError=null;
    const MAX_SIZE=10*1024*1024;

    if(file.size>MAX_SIZE){
      this.imageError='Picture is too big. Max size of picture is 10MB!'
      this.selectedFile=null;
      this.cdr.detectChanges();
    };
    const reader = new FileReader();
    reader.onload = () => {
      const result = reader.result;
      if (result && typeof result === 'string') {
        this.editData.profileImageUrl = result;
      }
      this.selectedFile = file;
      this.cdr.detectChanges();
    };
    reader.onerror = () => {
      this.imageError = 'Failed to read file.';
      this.selectedFile = null;
      this.cdr.detectChanges();
    };
    reader.readAsDataURL(file);
  }

  saveProfile(){
    const token=localStorage.getItem('authToken');
    const headers=new HttpHeaders({ 'Authorization': `Bearer ${token}`});

    const formData=new FormData();
    formData.append('username', this.editData.username || '');
    formData.append('email', this.editData.email || '');
    formData.append('phoneNumber', this.editData.phoneNumber || '');

    if(this.selectedFile){
      formData.append('file', this.selectedFile);
    }

    this.http.put<User>('http://127.0.0.1:8080/api/v1/users/me', formData, { headers }).subscribe({
      next: (responseUser)=>{
        console.log("ODGOVOR SA BACKENDA NAKON SAVE:", responseUser);
        this.user=responseUser;
        this.isEditing=false;
        this.selectedFile=null;
        this.imageError=null;
        this.cdr.detectChanges();
        alert('Profile succesfully updated');
      },
      error: (err)=>{
        console.log('Error while updating profile', err);
        alert('Error has occured');

      }
    })
  }

  deleteProduct(productId: number | undefined): void {
    if(!productId) return;
    if(confirm('Are you sure you want to delete this product?')){
        this.myProducts = this.myProducts.filter(p => p.id !== productId);
    }
  }

  onImageError() {
    if (this.user) {
      this.user.profileImageUrl = '';
    }
  }
}
