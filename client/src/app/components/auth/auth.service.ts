import { Injectable } from "@angular/core";
import { BehaviorSubject, catchError, tap, throwError } from "rxjs";
import { User } from "./user.model";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Router } from "@angular/router";


export interface AuthResponseData {
    token: string;
    email: string;
    expiresIn: string;
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private SIGNUP_API: string = "/api/v1/auth/signup";

    private LOGIN_API: string = "/api/v1/auth/login";

    user = new BehaviorSubject<User | null>(null);

    private tokenExpirationTimer: any;

    constructor(private httpClient: HttpClient, private router: Router) {}

    signup(email: string, password: string) {
        return this.httpClient.post<AuthResponseData>(this.SIGNUP_API,{
            email: email,
            password: password
        }).pipe(catchError(this.handleError), tap(resData => {
            this.handleAuthentication(resData.email, resData.token, +resData.expiresIn)
        }));
    }

    login(email: string, password: string) {
        return this.httpClient.post<AuthResponseData>(this.LOGIN_API,{
            email: email,
            password: password
        }).pipe(catchError(this.handleError), tap(resData => {
            this.handleAuthentication(resData.email, resData.token, +resData.expiresIn)
        }));
    }

    logout() {
        this.user.next(null);
        this.router.navigate(['/auth']);
        localStorage.removeItem('userData');
        if (this.tokenExpirationTimer) {
            clearTimeout(this.tokenExpirationTimer);
        }
        this.tokenExpirationTimer = null;
    }

    autoLogin() {
        const userData = localStorage.getItem('userData');

        if (!userData) {
            return;
        }

        const userDataConverted: { email: string; _token: string; _tokenExpirationDate: string } = JSON.parse(userData);
        const loadedUser = new User(userDataConverted.email, userDataConverted._token, new Date(userDataConverted._tokenExpirationDate));

        if (loadedUser.token) {
            this.user.next(loadedUser);
            const expirationDuration = new Date(userDataConverted._tokenExpirationDate).getTime() - new Date().getTime();
            this.autoLogout(expirationDuration);
        }
    }

    autoLogout(expirationDuration: number) {
        this.tokenExpirationTimer = setTimeout(() => {
           this.logout(); 
        }, expirationDuration);
    }

    private handleAuthentication(email: string, token: string, expiresIn: number) {
        const expirationDate = new Date(new Date().getTime() + expiresIn * 1000);
        const user = new User(email, token, expirationDate);
        this.user.next(user);
        localStorage.setItem('userData', JSON.stringify(user));
        this.autoLogout(expiresIn * 1000);
    }

    private handleError(errorRes: HttpErrorResponse) {
        console.log(errorRes);
        let errorMessage = 'An unknown error occurred!'
            // if (!errorRes.error || !errorRes.error.error) {
            //     return throwError(errorMessage);
            // }
            // switch (errorRes.error.error.message) {
            //     case 'EMAIL_EXISTS':
            //         errorMessage = 'This email exists already.';
            //         break;
            //     case 'EMAIL_NOT_FOUND':
            //         errorMessage = 'This email does not exist.';
            //         break;
            //     case 'INVALID_PASSWORD':
            //         errorMessage = 'This password is not correct';
            //         break;
            // }

            if (!errorRes.error) {
                return throwError(errorMessage);
            }
            switch (errorRes.error.message) {
                case 'Access Denied':
                    errorMessage = 'Access Denied';
                    break;
                case 'Email already exists':
                    errorMessage = 'Email Already Exists';
                    break;
                
            }
            
            return throwError(errorMessage);
    }

}