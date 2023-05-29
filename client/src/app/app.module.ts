import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { GoogleMapsModule } from '@angular/google-maps';
import { GooglemapComponent } from './components/googlemap/googlemap.component';
import { AppNavigationBarComponent } from './components/app-navigation-bar/app-navigation-bar.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ToiletDetailsComponent } from './components/toilet-details/toilet-details.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AuthComponent } from './components/auth/auth.component';
import { LoadingSpinnerComponent } from './components/loading-spinner/loading-spinner.component';
import { ReviewEditComponent } from './components/review-edit/review-edit.component';
import { AuthInterceptorService } from './components/auth/auth-interceptor.service';
import { DropdownDirective } from './dropdown.directive';
import { NavigationService } from './services/navigation.service';

@NgModule({
  declarations: [
    AppComponent,
    GooglemapComponent,
    AppNavigationBarComponent,
    ToiletDetailsComponent,
    AuthComponent,
    LoadingSpinnerComponent,
    ReviewEditComponent,
    DropdownDirective
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    GoogleMapsModule,
    ReactiveFormsModule,
    FormsModule
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptorService,
    multi: true
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
