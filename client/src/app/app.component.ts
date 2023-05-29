import { Component, OnInit } from '@angular/core';
import { AuthService } from './components/auth/auth.service';
import { NavigationService } from './services/navigation.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  constructor(private authService: AuthService, private navigationService: NavigationService) {}

  ngOnInit(): void {
    this.authService.autoLogin();
  }
 
}
