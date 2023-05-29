package ibf2022.batch1.csf.assessment.server.services;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ibf2022.batch1.csf.assessment.server.models.Review;
import ibf2022.batch1.csf.assessment.server.person.Person;
import ibf2022.batch1.csf.assessment.server.repositories.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public void saveReview(Review review) {

        // Set ID
        review.setId(UUID.randomUUID().toString());

        // Add user email to review
        Person person = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        review.setEmail(person.getEmail());

        // Set submittedDate
        review.setSubmittedDate(Instant.now());

        // Set active status
        review.setActive(true);

        reviewRepository.saveReview(review);
    }

    public List<Review> getReviewsByToiletName(String toiletName) {
        List<Review> reviewList = reviewRepository.getReviewsByToiletName(toiletName);
        return reviewList;
    }

    public void updateReview(String id, Review review) {
        // TODO: Need to confirm identity of user
        reviewRepository.updateReview(id, review);
    }

    public void deleteReview(String id) {
        // TODO: Need to confirm identity of user
        reviewRepository.deleteReview(id);
    }

    public List<Document> getAverageRatingForAllToilets() {
        return reviewRepository.getAverageRatingForAllToilets();
    }


    public List<Document> getAverageRatingByToiletName(String toiletName) {
        return reviewRepository.getAverageRatingByToiletName(toiletName);
    }


    
}
