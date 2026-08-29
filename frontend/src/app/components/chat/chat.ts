import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ChatService, Message } from '../../services/chat';
import { Subscription } from 'rxjs';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-chat',
  imports: [CommonModule, FormsModule],
  templateUrl: './chat.html',
  styleUrls: ['./chat.css']
})
export class ChatComponent implements OnInit, OnDestroy {
  messages: Message[] = []; 
  newMessage: string = '';
  recipientUsername: string = '';
  private messageSub!: Subscription;

  constructor(
    private chatService: ChatService,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.recipientUsername = this.route.snapshot.paramMap.get('username') || '';

    this.chatService.connect(); 

    if (this.recipientUsername) {
      this.chatService.getChatHistory(this.recipientUsername).subscribe({
        next: (data) => {
          this.messages = data;
          this.cdr.detectChanges();
        },
        error: (err) => console.error('Error while loading chat history', err)
      });
    }

    this.messageSub = this.chatService.getMessage().subscribe((msg: Message) => {
      if (
        msg.senderUsername === this.recipientUsername ||
        msg.receiverUsername === this.recipientUsername
      ) {
        this.messages.push(msg);
        this.cdr.detectChanges();
      }
    });
  }

  sendMessage() {
    if (!this.newMessage.trim() || !this.recipientUsername) return;

    this.chatService.sendMessage(this.recipientUsername, this.newMessage);
    this.newMessage = '';
    this.cdr.detectChanges();
  }

  ngOnDestroy(): void {
    if (this.messageSub) {
      this.messageSub.unsubscribe();
    }
  }
}