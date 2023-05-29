import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription, exhaustMap, take, tap } from 'rxjs';
import { Review } from 'src/app/models/review';
import { Toilet } from 'src/app/models/toilet';
import { ReviewService } from 'src/app/services/review.service';
import { ToiletService } from 'src/app/services/toilet.service';
import { User } from '../auth/user.model';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-toilet-details',
  templateUrl: './toilet-details.component.html',
  styleUrls: ['./toilet-details.component.css'],
})
export class ToiletDetailsComponent implements OnInit, OnDestroy {
  param$!: Subscription;
  toiletName = '';
  retrievedToilet!: Toilet;
  retrievedReviews: Review[] = [];
  isFetchingReviews = false;
  // User details
  user!: User | null;
  // For Modal
  isActivated = false;
  reviewForModal: Review | null = null;

  constructor(
    private activatedRoute: ActivatedRoute,
    private toiletService: ToiletService,
    private reviewService: ReviewService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.param$ = this.activatedRoute.params.subscribe((params) => {
      this.toiletName = params['name'];

      this.toiletService
        .getToiletByName(this.toiletName)
        .subscribe(toilet => this.retrievedToilet = toilet);
      });

      this.isFetchingReviews = true;
      this.authService.user.pipe(
        take(1),
        tap(user => this.user = user),
        exhaustMap(() => this.reviewService.getReviewsByToiletName(this.toiletName))
      ).subscribe(reviews => {
        this.retrievedReviews = reviews;
        this.isFetchingReviews = false;
      });
  }

  ngOnDestroy(): void {
    this.param$.unsubscribe();
  }

  onWriteAReview() {
    if (!this.user) {
      this.router.navigate(['/auth']);
    }

    this.isActivated = true;
  }

  onEdit(review: Review) {
    if (!this.user) {
      this.router.navigate(['/auth']);
    }
    this.reviewForModal = review;
    this.isActivated = true;
  }

  onDelete(id: string) {
    if (!this.user) {
      this.router.navigate(['/auth']);
    }
    this.isFetchingReviews = true;
    this.reviewService.deleteReview(id).pipe(
      exhaustMap(() => this.reviewService.getReviewsByToiletName(this.toiletName))
    ).subscribe(reviews => this.handleRefreshPage(reviews));
  }

  onClose(event: Review | null) {
    if (!event) {
      this.isActivated = false;
      return;
    }

    // if Review is returned, check whether I was in new mode or edit mode
    // If new mode, call create review method and call refresh method
    // If in edit mode, call update review method and call refresh method
    // Problem is if I also want to call update on the header
    // For now just refresh the list

    this.isFetchingReviews = true;

    if (!this.reviewForModal) {
      event.toiletName = this.toiletName;

      this.reviewService.postReview(event).pipe(
        exhaustMap(() => this.reviewService.getReviewsByToiletName(this.toiletName))
      ).subscribe(reviews => this.handleRefreshPage(reviews));
    } else {
      this.reviewService.updateReview(this.reviewForModal.id!, event).pipe(
        exhaustMap(() => this.reviewService.getReviewsByToiletName(this.toiletName))
      ).subscribe(reviews => this.handleRefreshPage(reviews));
    }
  }

  private handleRefreshPage(reviews: Review[]) {
    this.retrievedReviews = reviews;
    this.isFetchingReviews = false;
    this.isActivated = false;
  }
}
