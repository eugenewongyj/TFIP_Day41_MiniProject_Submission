import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Review, ReviewDto } from '../models/review';
import { map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  private REVIEW_API: string = "/api/v1/reviews";

  constructor(private httpClient: HttpClient) { }

  postReview(review: Review) {

    return this.httpClient.post<{ message: string}>(this.REVIEW_API, review);
    
  }

  getReviewsByToiletName(toiletName: string) {
    return this.httpClient.get<ReviewDto[]>(`${this.REVIEW_API}/${toiletName}`).pipe(
      map(reviewDtos => {
        return reviewDtos.map(reviewDto => {
          
          let review: Review = {...reviewDto, submittedDate: new Date(reviewDto['submittedDate'])};
          return review;

          // Alternative
          /*
          let review2: Review = {
            id: reviewDto.id,
            toiletName: reviewDto.toiletName,
            rating: reviewDto.rating,
            comment: reviewDto.comment,
            email: reviewDto.email,
            submittedDate: new Date()
          };
          */
        })
      })
    );
  }

  updateReview(id: string, review: Review) {
    return this.httpClient.put<{ message: string}>(`${this.REVIEW_API}/${id}`, review);
  }

  deleteReview(id: string) {
    return this.httpClient.put<{ message: string}>(`${this.REVIEW_API}/${id}/delete`, null);
  }
}
