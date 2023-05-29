import { Location } from "@angular/common";
import { Injectable } from "@angular/core";
import { NavigationEnd, Router } from "@angular/router";

@Injectable({
    providedIn: 'root'
})
export class NavigationService {

    private previousUrls = 0;

    constructor(private router: Router, private location: Location) {
        this.router.events.subscribe((event) => {
            if (event instanceof NavigationEnd) {
                this.previousUrls++;
            }
        });
    }

    back(): void {
        this.previousUrls--;
        if (this.previousUrls > 0) {
            this.location.back();
        } else {
            this.router.navigateByUrl("/");
        }
    }
}