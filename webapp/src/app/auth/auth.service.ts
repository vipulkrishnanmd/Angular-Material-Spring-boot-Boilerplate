import { Injectable } from '@angular/core';
import * as Rx from 'rxjs';
import { AuthState } from './auth.interface';
import { filter, map, skip } from 'rxjs/operators';

const AUTH_STATE_USERNAME = 'auth_state_username';
const AUTH_STATE_TOKEN = 'auth_state_token';

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private isInitialized: boolean;
  private authStateSubject$: Rx.ReplaySubject<AuthState | undefined>;

  get authState$(): Rx.Observable<AuthState | undefined> {
    if (!this.isInitialized) {
      this.init();
    }
    return this.authStateSubject$.asObservable();
  }

  init(): void {
    this.authStateSubject$ = new Rx.ReplaySubject(1);

    let username;
    let token;

    username = window.sessionStorage.getItem(AUTH_STATE_USERNAME);
    token = window.sessionStorage.getItem(AUTH_STATE_TOKEN);

    if (username && token) {
      this.authStateSubject$.next({ username, token });
    }

    // skip first one since the first one on init is already existing one
    this.authStateSubject$.pipe(skip(1)).subscribe((s) => {
      if (s) {
        window.sessionStorage.setItem(AUTH_STATE_USERNAME, s.username);
        window.sessionStorage.setItem(AUTH_STATE_TOKEN, s.token);
      }
    });
    this.isInitialized = true;
  }

  setSession(session: AuthState | undefined): void {
    if (!this.isInitialized) {
      this.init();
    }
    this.authStateSubject$.next(session);
  }

  getUsername(): Rx.Observable<string | undefined> {
    if (!this.isInitialized) {
      this.init();
    }
    return this.authState$.pipe(
      filter(x => x !== undefined),
      map((authState: AuthState) => authState.username)
    );
  }

  getAuthHeader(): Rx.Observable<string | undefined> {
    if (!this.isInitialized) {
      this.init();
    }
    return this.authState$.pipe(
      filter(x => x !== undefined),
      map((authState: AuthState) => `Bearer ${authState.token}`)
    );
  }
}
