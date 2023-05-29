import { Component, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, Subscription } from 'rxjs';
import { Review } from 'src/app/models/review';
import { ReviewService } from 'src/app/services/review.service';

@Component({
  selector: 'app-review-edit',
  templateUrl: './review-edit.component.html',
  styleUrls: ['./review-edit.component.css'],
})
export class ReviewEditComponent implements OnInit{

  form!: FormGroup;

  @Input()
  review!: Review | null

  @Output()
  close = new Subject<Review | null>();

  constructor(
    private formBuilder: FormBuilder,
    private reviewService: ReviewService,
  ) {}

  ngOnInit(): void {
    this.form = this.createForm();
  }

  private createForm(): FormGroup {
    let rating = null;
    let comment = '';

    if (this.review) {
      rating = this.review.rating;
      comment = this.review.comment;
    }

    return this.formBuilder.group({
      rating: this.formBuilder.control<number | null>(rating, [
        Validators.required,
        Validators.pattern('^[1-5]$')
      ]),
      comment: this.formBuilder.control<string>(comment),
    });
  }

  submit(): void {
    let reviewFromUser = this.form.value as Review;
    this.close.next(reviewFromUser);
  }
}
