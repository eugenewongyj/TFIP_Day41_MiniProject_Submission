package ibf2022.batch1.csf.assessment.server.utils;

import java.io.Reader;
import java.io.StringReader;
import java.time.Instant;
import java.util.List;

import org.bson.Document;

import ibf2022.batch1.csf.assessment.server.models.Review;
import ibf2022.batch1.csf.assessment.server.models.ToiletDto;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import static ibf2022.batch1.csf.assessment.server.Constants.*;

public class Utils {

    public static JsonObject jsonStringToJsonObject(String jsonString) {
        Reader reader = new StringReader(jsonString);
        JsonReader jsonReader = Json.createReader(reader);
        return jsonReader.readObject();
    }

    ///// Review Methods

    // Incoming
    public static Review jsonObjectToReview(JsonObject jsonObject) {
        Review review = new Review();
        review.setRating(jsonObject.getInt("rating"));
        review.setComment(jsonObject.getString("comment"));
        try {
            review.setToiletName(jsonObject.getString("toiletName"));
        } catch (Exception e) {
            
        }
        return review;
    }

    public static JsonArray reviewListToJsonArray(List<Review> reviewList) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        reviewList.stream()
                .map(Utils::reviewToJsonObject)
                .forEach(jsonArrayBuilder::add);
        return jsonArrayBuilder.build();
    }

    public static JsonObject reviewToJsonObject(Review review) {
        return Json.createObjectBuilder()
            .add("id", review.getId())
            .add("toiletName", review.getToiletName())
            .add("rating", review.getRating())
            .add("comment", review.getComment())
            .add("email", review.getEmail())
            .add("submittedDate", review.getSubmittedDate().toString())
            .build();
    }

    public static Document reviewToDocument(Review review) {
        Document document = new Document();
        document.put(FIELD_ID, review.getId());
        document.put(FIELD_TOILETNAME, review.getToiletName());
        document.put(FIELD_RATING, review.getRating());
        document.put(FIELD_COMMENT, review.getComment());
        document.put(FIELD_EMAIL, review.getEmail());
        document.put(FIELD_SUBMITTEDDATE, review.getSubmittedDate().toString());
        document.put(FIELD_ISACTIVE, review.isActive());
        return document;
    }

    public static List<Review> documentListToReviewList(List<Document> documentList) {
        return documentList.stream()
            .map(Utils::documentToReview)
            .toList();
    }

    public static Review documentToReview(Document document) {
        Review review = new Review();
        review.setId(document.getString(FIELD_ID));
        review.setToiletName(document.getString(FIELD_TOILETNAME));
        review.setRating(document.getInteger(FIELD_RATING));
        review.setComment(document.getString(FIELD_COMMENT));
        review.setEmail(document.getString(FIELD_EMAIL));
        review.setSubmittedDate(Instant.parse(document.getString(FIELD_SUBMITTEDDATE)));
        review.setActive(document.getBoolean(FIELD_ISACTIVE));
        return review;
    }

    ///// Toilet Methods

    public static JsonArray toiletDtoListToJsonArray(List<ToiletDto> toiletList) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        toiletList.stream()
                .map(Utils::toiletDtoToJsonObject)
                .forEach(jsonArrayBuilder::add);
        return jsonArrayBuilder.build();
    }

    public static JsonObject toiletDtoToJsonObject(ToiletDto toiletDto) {
        return Json.createObjectBuilder()
                .add("id", toiletDto.getId())
                .add("name", toiletDto.getName())
                .add("address", toiletDto.getAddress())
                .add("latitude", toiletDto.getLatitude())
                .add("longitude", toiletDto.getLongitude())
                .add("count", toiletDto.getReviewSummaryDto().getCount())
                .add("average", toiletDto.getReviewSummaryDto().getAverage())
                .build();
    }

   
    
}
