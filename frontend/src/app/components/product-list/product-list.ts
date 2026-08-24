import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../services/product';
import { CategoryService } from '../../services/category';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-product-list',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './product-list.html',
  styleUrl: './product-list.css',
})
export class ProductListComponent implements OnInit{
  products: any[]=[];
  categories: any[]=[];
  errorMessage='';
  searchQuery: string='';
  selectedCategoryId: number | null = null;

  constructor(private productService: ProductService, 
              private cdr: ChangeDetectorRef, 
              private categoryService: CategoryService) {}

  ngOnInit(): void {
    this.loadProducts();
    this.loadCategories();
  }

  loadProducts(){
    this.productService.getProducts().subscribe({
      next: (data)=>{
        this.products=data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage='Error while loading products.'
        console.error(err);
        this.cdr.detectChanges();
      }
    })
  }

  loadCategories(){
    this.categoryService.getCategories().subscribe({
      next: (data)=>{
        this.categories=data;
        this.cdr.detectChanges();
      },
      error: (err)=>{
        this.errorMessage='Error while loading categories.'
        console.error(err);
        this.cdr.detectChanges();
      }
    });
  }

  onSearch() {
    const query = this.searchQuery ? this.searchQuery.trim() : '';

    if (!query) {
      this.loadProducts();
      return;
    }

    this.productService.searchProducts(query).subscribe({
      next: (data) => {
        this.products = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error searching products', err);
        this.cdr.detectChanges();
      }
    });
  }

  filterByCategory(categoryId: number | null){
    this.selectedCategoryId=categoryId;

    if(categoryId===null){
      this.loadProducts();
      return;
    }

    this.productService.getProductByCategory(categoryId).subscribe({
      next: (data)=>{
        this.products=data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log(err);
        this.cdr.detectChanges();
      }
    });
  }
}
