
import Keycloak from 'keycloak-js';
import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Injectable({ providedIn: 'root' })
export class AuthService {
  keycloak: any;
  private isBrowser: boolean;
  private _isLoggedIn: boolean = false;
  private initPromise: Promise<boolean> | null = null;

  constructor(@Inject(PLATFORM_ID) platformId: Object) {
    this.isBrowser = platformId === 'browser' || isPlatformBrowser(platformId);
  }

  init(): Promise<boolean> {
    if (!this.isBrowser) return Promise.resolve(false);
    if (this.initPromise) return this.initPromise!; // ✅ FIX 1

    const storedToken        = sessionStorage.getItem('mg_token')         || undefined;
    const storedRefreshToken = sessionStorage.getItem('mg_refresh_token') || undefined;
    const storedIdToken      = sessionStorage.getItem('mg_id_token')      || undefined;

    // ✅ FIX 2: allow init during OAuth callback
    const isOAuthCallback =
      window.location.hash.includes('state=') ||
      window.location.search.includes('state=') ||
      window.location.hash.includes('session_state=');

    if (!storedToken && !storedRefreshToken && !isOAuthCallback) {
      this._isLoggedIn = false;
      this.initPromise = Promise.resolve(false);
      return this.initPromise!;
    }

    this.keycloak = new Keycloak({
      url: 'https://34.14.214.173.nip.io/auth',
      realm: 'microguard',
      clientId: 'microguard-client'
    });

    this.initPromise = this.keycloak.init({
      onLoad: 'check-sso',
      checkLoginIframe: false,
      token:        storedToken,
      refreshToken: storedRefreshToken,
      idToken:      storedIdToken
    }).then((authenticated: boolean) => {
      this._isLoggedIn = authenticated;
      if (authenticated) {
        sessionStorage.setItem('mg_auth',          'true');
        sessionStorage.setItem('mg_token',         this.keycloak.token         || '');
        sessionStorage.setItem('mg_refresh_token', this.keycloak.refreshToken  || '');
        sessionStorage.setItem('mg_id_token',      this.keycloak.idToken       || '');
      } else {
        sessionStorage.removeItem('mg_auth');
        sessionStorage.removeItem('mg_token');
        sessionStorage.removeItem('mg_refresh_token');
        sessionStorage.removeItem('mg_id_token');
      }
      return authenticated;
    }).catch(() => {
      this._isLoggedIn = false;
      sessionStorage.removeItem('mg_auth');
      sessionStorage.removeItem('mg_token');
      sessionStorage.removeItem('mg_refresh_token');
      sessionStorage.removeItem('mg_id_token');
      return false;
    });

    return this.initPromise!;
  }

  login(): any {
    if (!this.isBrowser) return;

    const kc = new Keycloak({
      url: 'https://34.14.214.173.nip.io/auth',
      realm: 'microguard',
      clientId: 'microguard-client'
    });

    kc.init({ checkLoginIframe: false }).then((authenticated: boolean) => {
      if (authenticated) {
        // Already logged in — store tokens and go to dashboard
        sessionStorage.setItem('mg_auth', 'true');
        sessionStorage.setItem('mg_token', kc.token || '');
        sessionStorage.setItem('mg_refresh_token', kc.refreshToken || '');
        sessionStorage.setItem('mg_id_token', kc.idToken || '');
        window.location.href = 'https://34.14.214.173.nip.io/dashboard';
      } else {
        kc.login({
          redirectUri: 'https://34.14.214.173.nip.io/dashboard',
          idpHint: 'google'
        });
      }
    });
  }

  register(options?: any): any {
    if (!this.isBrowser || !this.keycloak) return;
    this.keycloak.login({
      redirectUri: 'https://34.14.214.173.nip.io/dashboard',
      idpHint: 'google',
      ...options
    });
  }

  logout() {
    if (!this.isBrowser || !this.keycloak) return;
    this._isLoggedIn = false;
    this.initPromise = null;
    sessionStorage.removeItem('mg_auth');
    sessionStorage.removeItem('mg_token');
    sessionStorage.removeItem('mg_refresh_token');
    sessionStorage.removeItem('mg_id_token');

    this.keycloak.logout({ redirectUri: 'https://34.14.214.173.nip.io/' });
  }

  isLoggedIn(): boolean {
    if (!this.isBrowser) return false;
    return this._isLoggedIn || sessionStorage.getItem('mg_auth') === 'true';
  }

  async getValidToken(): Promise<string> {
    if (!this.isBrowser || !this.keycloak) return '';
    if (!this.isLoggedIn()) return '';

    try {
      await this.keycloak.updateToken(-1);
      const token = this.keycloak.token || '';
      if (token) {
        sessionStorage.setItem('mg_token',         token);
        sessionStorage.setItem('mg_refresh_token', this.keycloak.refreshToken || '');
        sessionStorage.setItem('mg_id_token',      this.keycloak.idToken      || '');
      }
      return token;
    } catch (e) {
      this._isLoggedIn = false;
      this.initPromise = null;
      sessionStorage.removeItem('mg_auth');
      sessionStorage.removeItem('mg_token');
      sessionStorage.removeItem('mg_refresh_token');
      sessionStorage.removeItem('mg_id_token');

      this.keycloak.login({
        redirectUri: 'https://34.14.214.173.nip.io/dashboard',
        idpHint: 'google'
      });

      return '';
    }
  }

  getUserId(): string {
    if (!this.isBrowser || !this.keycloak) return '';
    return this.keycloak.tokenParsed?.sub || '';
  }

  getEmail(): string {
    if (!this.isBrowser || !this.keycloak) return '';
    return this.keycloak.tokenParsed?.email || '';
  }

  getName(): string {
    if (!this.isBrowser || !this.keycloak) return '';
    return this.keycloak.tokenParsed?.preferred_username
      || this.keycloak.tokenParsed?.name
      || '';
  }
}

