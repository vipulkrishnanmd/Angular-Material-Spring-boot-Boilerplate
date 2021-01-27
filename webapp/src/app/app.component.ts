import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <app-navbar> </app-navbar>
    <router-outlet></router-outlet>
  `,
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  title = 'workflows';
}
