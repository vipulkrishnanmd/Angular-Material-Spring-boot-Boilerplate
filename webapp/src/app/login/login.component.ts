import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Login } from './login.service';

@Component({
  selector: 'app-login',
  template: `
    <mat-card>
      <mat-card-content>
        <form [formGroup]="form" (ngSubmit)="onSubmit()">
          <h2>Log In</h2>
          <mat-error *ngIf="loginInvalid">
            The username and password were not recognised
          </mat-error>
          <mat-form-field class="full-width-input">
            <input
              matInput
              placeholder="Email"
              formControlName="username"
              required
            />
            <mat-error> Please provide a valid email address </mat-error>
          </mat-form-field>
          <mat-form-field class="full-width-input">
            <input
              matInput
              type="password"
              placeholder="Password"
              formControlName="password"
              required
            />
            <mat-error> Please provide a valid password </mat-error>
          </mat-form-field>
          <button mat-raised-button color="primary">Login</button>
        </form>
      </mat-card-content>
    </mat-card>
  `,
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  form: FormGroup;
  loginInvalid = false;
  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private loginService: Login,
  ) {}
  ngOnInit(): void {
    this.form = this.fb.group({
      username: ['', Validators.email],
      password: ['', Validators.required],
    });
  }

  onSubmit(): void {
    const credentials = this.form.getRawValue();
    this.loginService.login(credentials.username, credentials.password);
  }
}
