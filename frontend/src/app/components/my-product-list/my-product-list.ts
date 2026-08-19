import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Product } from '../../models/product';
import { ProductService } from '../../services/product';
import { UserService } from '../../services/user';

@Component({
  selector: 'app-my-product-list',
  imports: [CommonModule, FormsModule],
  templateUrl: './my-product-list.html',
  styleUrl: './my-product-list.css',
})
export class MyProductListComponent implements OnInit {

  myProducts: Product[] =[];
  isLoading: boolean=false;
  constructor(private productService: ProductService,
              private userService: UserService,
              private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.loadMyProducts();
  }

  loadMyProducts() {
    this.userService.getCurrentUserInfo().subscribe({
      next: (user) => {
        console.log("KLJUČNI PODACI - Ulogovani korisnik:", user);
        console.log("ID korisnika koji se šalje:", user?.id);

        if (user && user.id) {
          this.productService.getProductByUserId(user.id).subscribe({
            next: (products) => {
              console.log("Proizvodi vraćeni sa bekenda:", products);
              this.myProducts = products;
              this.isLoading = false;
              this.cdr.detectChanges();
            },
            error: (err) => {
              console.error('Greška pri učitavanju proizvoda (404 ili drugo):', err);
              this.isLoading = false;
              this.cdr.detectChanges();
            }
          });
        } else {
          console.warn("Upozorenje: Korisnik nema ID ili objekat korisnika nije stigao dobro!");
          this.isLoading = false;
          this.cdr.detectChanges();
        }
      },
      error: (err) => {
        console.error('Greška pri učitavanju ulogovanog korisnika:', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  deleteProduct(id: number) {
    if (confirm('Are you sure you want to delete this product')) {
      this.productService.deleteProduct(id).subscribe({
        next: () => {
          this.myProducts = this.myProducts.filter(p => p.id !== id);
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Error while loading product', err);
          alert('Error has occured while loading an product');
          this.cdr.detectChanges();
        }
      });
    }
  }
}
  

