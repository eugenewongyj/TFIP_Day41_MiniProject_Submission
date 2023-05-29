package ibf2022.batch1.csf.assessment.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibf2022.batch1.csf.assessment.server.models.Review;
import ibf2022.batch1.csf.assessment.server.services.ReviewService;
import ibf2022.batch1.csf.assessment.server.utils.Utils;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    @CrossOrigin(origins="*")
    public ResponseEntity<String> postReview(@RequestBody String payload) {
        JsonObject jsonObject = Utils.jsonStringToJsonObject(payload);
        Review review = Utils.jsonObjectToReview(jsonObject);
        reviewService.saveReview(review);

        JsonObject response = Json.createObjectBuilder()
                                .add("message", "Review Saved")
                                .build();
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(response.toString());
    }

    @GetMapping(path="/{toiletName}")
    @CrossOrigin(origins="*")
    public ResponseEntity<String> getReviewsByToiletName(@PathVariable String toiletName) {
        List<Review> reviewList = reviewService.getReviewsByToiletName(toiletName);
        JsonArray jsonArray = Utils.reviewListToJsonArray(reviewList);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(jsonArray.toString());
    }

    @PutMapping(path="/{id}")
    @CrossOrigin(origins="*")
    public ResponseEntity<String> updateReview(@PathVariable String id, @RequestBody String payload) {
        JsonObject jsonObject = Utils.jsonStringToJsonObject(payload);
        Review review = Utils.jsonObjectToReview(jsonObject);
        reviewService.updateReview(id, review);
        JsonObject response = Json.createObjectBuilder()
                                .add("message", "Review Updated")
                                .build();
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response.toString());
    }

    @PutMapping(path="/{id}/delete")
    @CrossOrigin(origins="*")
    public ResponseEntity<String> deleteReview(@PathVariable String id) {
        reviewService.deleteReview(id);
        JsonObject response = Json.createObjectBuilder()
                                .add("message", "Review Deleted")
                                .build();
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response.toString());
    }
}
