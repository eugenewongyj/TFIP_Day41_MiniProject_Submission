import { HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, exhaustMap, take } from "rxjs";
import { AuthService } from "./auth.service";

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {

    private POST_REVIEW_API: string = "/api/v1/reviews";

    constructor(private authService: AuthService) {}

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return this.authService.user.pipe(take(1),
            exhaustMap((user: any) => {
                
                if (req.url === this.POST_REVIEW_API) {
                    const modifiedHttpHeaders = req.headers.set('Authorization', `Bearer ${user.token}`);
                    const modifiedReq = req.clone({ headers: modifiedHttpHeaders })
                    return next.handle(modifiedReq);
                }

                return next.handle(req);
            }))
    }
    
}