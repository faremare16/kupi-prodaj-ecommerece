import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ProductService } from '../../services/product';

@Component({
  selector: 'app-product-details',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './product-details.html',
  styleUrl: './product-details.css',
})
export class ProductDetailsComponent implements OnInit{
  product: any=null;
  isLoading: boolean=true;

  constructor(
    private router: ActivatedRoute,
    private productService: ProductService,
    private cdr: ChangeDetectorRef
    ) {}

    ngOnInit(): void {
      this.loadProductDetails();
    }

    loadProductDetails(){
      const id=Number(this.router.snapshot.paramMap.get('id'));
      if(id){
        this.productService.getProductById(id).subscribe({
          next:(data)=>{
            this.product=data;
            this.isLoading=false;
            this.cdr.detectChanges();
          },
          error: (err)=>{
            console.error('Error while trying to load product details', err);
            this.isLoading=false;
            this.cdr.detectChanges();
          }
        })
      }

    }
}
