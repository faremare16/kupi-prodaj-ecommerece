import { ChangeDetectorRef, Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

export interface Message{
    id?: number;
    content: string;
    timeStamp?: string;
    senderUsername: string;
    receiverUsername: string;
}

@Injectable({
    providedIn:'root'
})

export class ChatService {
    private stompClient: any;
    private messageSubject=new Subject<Message>();
    private apiUrl=`${environment.apiUrl}/chat`;

    constructor(private http: HttpClient){}

    // povezivanje na WebSocket 
    connect() {
    const socket = new SockJS(`${environment.apiUrl}/ws`);
    this.stompClient = Stomp.over(socket);

    this.stompClient.connect({}, () => {
        this.stompClient.subscribe(`/user/topic/messages`, (sdkEvent: any) => {
        const message: Message = JSON.parse(sdkEvent.body);
        this.messageSubject.next(message);
        });
    }, (error: any) => {
        console.error('WebSocket error ', error);
    });
    }

    // slanje poruka preko webSocket-a
    sendMessage(receiverUsername: string, content: string){
        const chatMessage = {
            receiverUsername: receiverUsername,
            content: content
        };
        
        this.stompClient.send('/app/chat', {}, JSON.stringify(chatMessage));
    }

    // pracenje novih poruka
    getMessage(): Observable<Message> {
        return this.messageSubject.asObservable();
    }

    // ucitavanje stare historije
    getChatHistory(recipientUsername: string): Observable<Message[]> {
        return this.http.get<Message[]>(`${this.apiUrl}/history/${recipientUsername}`, {
            withCredentials: true 
        });
    }

    getConversations(): Observable<any[]> {
        return this.http.get<any[]>(`${environment.apiUrl}/chat/conversations`, {
            withCredentials: true
        });
}
}



