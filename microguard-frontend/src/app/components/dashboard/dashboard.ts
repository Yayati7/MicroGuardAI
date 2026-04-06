import { Component, OnInit, Inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { ApiService } from '../../services/api.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './dashboard.html'
})
export class DashboardComponent implements OnInit {

  name = '';
  services: string[] = [];
  database = '';
  communication = '';
  history: any[] = [];
  historyLoaded = false;

  constructor(
    private api: ApiService,
    private auth: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit() {
    if (!isPlatformBrowser(this.platformId)) return;

    this.historyLoaded = false;
    this.history = [];

    this.auth.init().then(authenticated => {

      // ✅ authenticated = true on first Google login redirect
      // ✅ isLoggedIn() = true on back navigation (from sessionStorage)
      const loggedIn = authenticated || this.auth.isLoggedIn();

      if (!loggedIn) {
        this.router.navigate(['/login']);
        return;
      }

      this.api.registerMe().catch(() => {});
      this.loadHistory();

    }).catch(() => {
      if (!this.auth.isLoggedIn()) {
        this.router.navigate(['/login']);
      } else {
        this.api.registerMe().catch(() => {});
        this.loadHistory();
      }
    });
  }

  loadHistory() {
    this.api.getProjects().then(res => {
      this.history = res.data || [];
      this.historyLoaded = true;
      this.cdr.detectChanges();
    }).catch(() => {
      this.historyLoaded = true;
      this.cdr.detectChanges();
    });
  }

  logout() {
    this.auth.logout();
  }

  addProject() {
    if (!isPlatformBrowser(this.platformId)) return;

    const data = {
      name: this.name,
      services: this.services,
      database: this.database,
      communication: this.communication
    };

    this.api.createArchitecture(data).then(res => {
      const projectId = res?.data?.projectId;
      if (!projectId) { console.error('❌ PROJECT ID is null'); return; }

      this.api.createProject({
        projectId: projectId,
        projectName: this.name,
        architectureJson: JSON.stringify(data)
      }).catch(err => console.error('Save project error:', err));

      this.router.navigate(['/analysis', projectId]);
    }).catch(err => console.error('API ERROR:', err));
  }

  viewHistory(projectId: string) {
    this.router.navigate(['/analysis', projectId]);
  }
}