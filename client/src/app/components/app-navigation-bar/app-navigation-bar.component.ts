import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-app-navigation-bar',
  templateUrl: './app-navigation-bar.component.html',
  styleUrls: ['./app-navigation-bar.component.css']
})
export class AppNavigationBarComponent implements OnInit, OnDestroy {

  isAuthenticated = false;

  private userSub!: Subscription;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.userSub = this.authService.user.subscribe(user => {
      this.isAuthenticated = !!user;
    });
  }

  ngOnDestroy(): void {
    this.userSub.unsubscribe();
  }

  onLogout() {
    this.authService.logout();
  }

}
