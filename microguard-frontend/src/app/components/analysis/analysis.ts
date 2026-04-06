import { Component, OnInit, NgZone, ChangeDetectorRef, Inject, PLATFORM_ID } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-analysis',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './analysis.html',
  styleUrls: ['./analysis.css'] 
})
export class AnalysisComponent implements OnInit {

  projectId: string = '';
  result: any[] = [];
  loading = true;

  constructor(
    private route: ActivatedRoute,
    private api: ApiService,
    private zone: NgZone,
    private auth: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit() {
    if (!isPlatformBrowser(this.platformId)) return;

    // ✅ REMOVED auth redirect guard
    // Keycloak handles session; if expired → API fails gracefully

    const id = this.route.snapshot.paramMap.get('id');
    if (!id) { console.error('❌ PROJECT ID is null'); return; }
    this.projectId = id;

    // Try Postgres first
    this.api.getProjects().then(res => {
      const projects = res.data || [];
      const match = projects.find((p: any) => p.projectId === id);

      if (match && match.aiResult) {
        this.zone.run(() => {
          this.result = [{ result: match.aiResult }];
          this.loading = false;
          this.cdr.detectChanges();
        });
      } else {
        this.fetchAnalysisWithRetry();
      }
    }).catch(() => this.fetchAnalysisWithRetry());
  }

  fetchAnalysisWithRetry(retries = 15) {
    this.api.getAnalysis(this.projectId).then(res => {
      if (res.data && res.data.length > 0) {
        this.zone.run(() => {
          this.result = [...res.data];
          this.loading = false;
          this.cdr.detectChanges();
        });
        return;
      }
      if (retries > 0) {
        setTimeout(() => this.fetchAnalysisWithRetry(retries - 1), 2000);
      } else {
        this.zone.run(() => { this.loading = false; this.cdr.detectChanges(); });
      }
    }).catch(() => {
      this.zone.run(() => { this.loading = false; this.cdr.detectChanges(); });
    });
  }
}