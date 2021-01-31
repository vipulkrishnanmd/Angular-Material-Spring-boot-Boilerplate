import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { take } from 'rxjs/operators';
import { AuthState } from '../auth/auth.interface';
import { Auth } from '../auth/auth.service';

@Injectable({
  providedIn: 'root',
})
export class Login {
  constructor(private http: HttpClient, private auth: Auth) {}

  login(username: string, password: string): void {
    console.log('reaching here');
    this.http
      // TODO: move this to env
      .post('http://localhost:8080/api/auth/v1/login', {
        username,
        password,
      })
      .pipe(take(1))
      .toPromise()
      .then((x: AuthState) => this.auth.setSession(x));

    this.auth.getAuthHeader().subscribe(x=>console.log(x));
  }
}
