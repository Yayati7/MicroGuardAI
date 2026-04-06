import { Injectable } from '@angular/core';
import axios from 'axios';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  // ✅ CHANGED: localhost → VM IP
  private baseUrl = 'https://34.14.214.173.nip.io';

  constructor(private auth: AuthService) {}

  private async getHeaders() {
    const token = await this.auth.getValidToken();
    return { Authorization: 'Bearer ' + token };
  }

  async registerMe() {
    const headers = await this.getHeaders();
    return axios.post(`${this.baseUrl}/api/users/me`, {}, { headers });
  }

  async createArchitecture(data: any) {
    const headers = await this.getHeaders();
    return axios.post(`${this.baseUrl}/api/architecture`, data, { headers });
  }

  async createProject(data: { projectId: string, projectName: string, architectureJson: string }) {
    const headers = await this.getHeaders();
    return axios.post(`${this.baseUrl}/api/projects`, data, { headers });
  }

  async getProjects() {
    const headers = await this.getHeaders();
    return axios.get(`${this.baseUrl}/api/projects`, { headers });
  }

  async getAnalysis(projectId: string) {
    const headers = await this.getHeaders();
    return axios.get(`${this.baseUrl}/api/analysis/${projectId}`, { headers });
  }
}