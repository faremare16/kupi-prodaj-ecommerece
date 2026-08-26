import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { finalize } from 'rxjs';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-add-item',
  imports: [CommonModule, FormsModule, RouterModule, ReactiveFormsModule],
  templateUrl: './add-item.html',
  styleUrl: './add-item.css',
})
export class AddItemComponent implements OnInit{
    productForm!: FormGroup;
  

  categories:any[]=[];

  selectedFile: File | null=null;
  imageError: string | null=null;
  imagePreview: string | null=null;
  isLoading: boolean=false;


  constructor(private http: HttpClient, 
              private cdr: ChangeDetectorRef,
              private router: Router,
              private fb: FormBuilder){}

  ngOnInit(): void {
    this.loadCategories();

    this.productForm=this.fb.group({
      name: ['', Validators.required],
      categoryId: ['', Validators.required],
      description: [''],
      price: ['', [Validators.required, Validators.min(0)]],
      unitsInStock: ['', [Validators.required, Validators.min(0)]],
      file: [null]
    })
  }
   // uctivanje kategorija, za padajući meni
  loadCategories(){
    this.http.get<any[]>(`${environment.apiUrl}/categories`).subscribe({
      next: (data)=>{
        this.categories=data;
      },
      error: (err)=>{
        console.error('Error while loading categories.', err);
      }
    });
  }

  // selektovanje slike
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
      return;
    };
    const reader = new FileReader();
    reader.onload = () => {
      const result = reader.result;
      if (result && typeof result === 'string') {
        this.imagePreview=result;
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

  // slanje kompletne forme i slike na backend
  onSubmit(){
    if(this.productForm.invalid){
      this.productForm.markAllAsTouched();
      return;
    }
    
    this.isLoading=true;

    const formData=new FormData();
    formData.append('name', this.productForm.get('name')?.value);
    formData.append('categoryId', this.productForm.get('categoryId')?.value);
    formData.append('description', this.productForm.get('description')?.value);
    formData.append('price', this.productForm.get('price')?.value);
    formData.append('unitsInStock', this.productForm.get('unitsInStock')?.value);

    if(this.selectedFile){
      formData.append('file', this.selectedFile);
    }

    this.http.post(`${environment.apiUrl}/products`, formData, {
      withCredentials: true
    })
      .pipe(
        finalize(() => {
          this.isLoading=false;
          this.cdr.detectChanges();
        })
      )
      .subscribe({
      next:()=>{
        this.router.navigate(['/product-list']);
      },
      error: (err)=>{
        console.log('Error while creating an product.', err);
        alert('Error while creating an product.');
      }
    })
  }
}
