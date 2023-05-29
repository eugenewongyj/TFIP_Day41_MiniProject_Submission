import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { GooglemapComponent } from './components/googlemap/googlemap.component';
import { ToiletDetailsComponent } from './components/toilet-details/toilet-details.component';
import { AuthComponent } from './components/auth/auth.component';
import { ReviewEditComponent } from './components/review-edit/review-edit.component';
import { AuthGuard } from './components/auth/auth.guard';

const routes: Routes = [
  { path: '', redirectTo: '/map', pathMatch: 'full' },
  { path: 'map', component: GooglemapComponent },
  { path: 'toilets/:name', component: ToiletDetailsComponent },
  { path: 'toilets/:toiletName/review', component: ReviewEditComponent, canActivate: [ AuthGuard ] },
  { path: 'auth', component: AuthComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
