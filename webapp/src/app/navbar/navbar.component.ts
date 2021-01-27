import { Component } from '@angular/core';

@Component({
  selector: `app-navbar`,
  template: `
    <mat-toolbar color="primary">
      <mat-toolbar-row>
        <button mat-icon-button>
          <mat-icon>menu</mat-icon>
        </button>
        <h1>Workflows</h1>
        <span class="menu-spacer"></span>
        <div>
          <a mat-button href="/login"> Login </a>
          <a mat-button> Signup </a>
        </div>
      </mat-toolbar-row>
    </mat-toolbar>
  `,
  styleUrls: [],
})
export class NavbarComponent {}
