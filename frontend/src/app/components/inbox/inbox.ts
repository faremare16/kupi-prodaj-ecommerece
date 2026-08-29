import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ChatService } from '../../services/chat';

@Component({
  selector: 'app-inbox',
  imports: [CommonModule, RouterModule],
  templateUrl: './inbox.html',
  styleUrl: './inbox.css',
})
export class InboxComponent implements OnInit {
	  conversations: any[] = [];
	  isLoading = true;
	
	  constructor(
	    private chatService: ChatService,
	    private cdr: ChangeDetectorRef
	  ) {}
	
	  ngOnInit(): void {
	    this.loadConversations();
	  }
	
	  loadConversations(): void {
	    this.chatService.getConversations().subscribe({
	      next: (data) => {
	        this.conversations = data;
	        this.isLoading = false;
	        this.cdr.detectChanges();
	      },
	      error: (err) => {
	        console.error('Greška pri učitavanju razgovora:', err);
	        this.isLoading = false;
	        this.cdr.detectChanges();
	      }
	    });
	  }
	}
