import { User } from './../../../model/user';
import { AuthService } from './../../services/auth.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css'],
})
export class NavComponent {
  showAdmin = false;
  isLoggedIn = false;
  userLabel? = '';

  constructor(private authService: AuthService) {}

  ngOnInit() {
    const jwt = this.authService.getToken();
    if (jwt != null) {
      this.getUserDetails();
    }
  }

  private getUserDetails() {
    const user: User = this.authService.getCurrentUser();
    if (user != null) {
      this.isLoggedIn = true;
      this.userLabel = user.username;
      if (this.authService.isAdminUser(user)) {
        this.showAdmin = true;
      }
    }
  }

  public logout() {
    this.authService.logout();
    window.location.reload();
  }
}