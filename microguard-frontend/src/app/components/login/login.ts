import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',   // ✅ USE HTML FILE
  styleUrls: ['./login.css']     // ✅ LOAD CSS
})
export class LoginComponent {

  constructor(private auth: AuthService) {}

  login() {
    (this.auth as any).login({
      redirectUri: 'http://localhost:4200/dashboard'
    });
  }
}