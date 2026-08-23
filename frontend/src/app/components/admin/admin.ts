import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../services/admin';
import { User } from '../../models/user';

@Component({
  selector: 'app-admin',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin.html',
  styleUrl: './admin.css',
})
export class AdminComponent implements OnInit{
  users: User[]=[];

  constructor(private adminService: AdminService, private cdr: ChangeDetectorRef){}

  // ngOnInit se izvrsava automatski cim se stranica pokrene
  ngOnInit(): void {
    this.getAllUsers();
  }

  getAllUsers(){
    this.adminService.getUsers().subscribe({
      // ako je zahtjev uspjesan (200 SUCCES) server vraca podatke
      next: (data: User[]) => {
        this.users = data;
        console.log('Succesfully loaded user: ', this.users);
        this.cdr.detectChanges();
      },
      // ako je zahtjev neuspjesan server vraca error
      error: (err) => {
        console.error('Error while getting users.', err);
      }
    });
  }

  deleteUser(id: number): void{
    // prozor za potrvrdu
    if(confirm('Are you sure you want to delete this user from database?')){
      this.adminService.deleteUser(id).subscribe({
        next: () =>{
          console.log('User succesfully deleted with id: ', id);
          this.getAllUsers();
          this.cdr.detectChanges();
        },
        error: (err)=>{
          console.log('Error while deleting user', err);
        }
      });
    }
  }
}
