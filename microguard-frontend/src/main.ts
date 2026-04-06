import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';

// ✅ Just bootstrap directly — no manual AuthService creation
// AuthService is provided in appConfig.providers and Angular manages ONE instance
bootstrapApplication(App, appConfig).catch(err => console.error(err));