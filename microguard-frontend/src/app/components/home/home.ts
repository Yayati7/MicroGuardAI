import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router'; 

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [FormsModule, RouterModule],
  templateUrl: './home.html',
  styleUrls: ['./home.css'], 
})
export class HomeComponent {}
