package ibf2022.batch1.csf.assessment.server.repositories;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;



import ibf2022.batch1.csf.assessment.server.models.Review;
import ibf2022.batch1.csf.assessment.server.utils.Utils;
import static ibf2022.batch1.csf.assessment.server.Constants.*;

import java.util.List;

@Repository
public class ReviewRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveReview(Review review) {
        Document document = Utils.reviewToDocument(review);
        mongoTemplate.insert(document, COLLECTION_REVIEW);
    }
    
    /* 
    db.review.find.({
        toiletName: 'toiletName',
        isActive: true
    })
    */

    public List<Review> getReviewsByToiletName(String toiletName) {
        Criteria criteria = Criteria
            .where(FIELD_TOILETNAME).is(toiletName)
            .and(FIELD_ISACTIVE).is(true);
        
        Query query = Query.query(criteria)
            .with(Sort.by(Sort.Direction.DESC, FIELD_SUBMITTEDDATE));

        List<Document> result = mongoTemplate.find(query, Document.class, COLLECTION_REVIEW);

        return Utils.documentListToReviewList(result);
    }

    /*
    db.review.update(
        {
            id: "uuid" 
}       ,
        {
            $set: { rating: 3 },
            $set: { comment: "new comment" }
        }
    )
    */
    public void updateReview(String id, Review review) {
        Criteria criteria = Criteria.where(FIELD_ID).is(id);
        Query query = Query.query(criteria);

        Update updateOps = new Update()
            .set("rating", review.getRating())
            .set("comment", review.getComment());

        mongoTemplate.updateFirst(query, updateOps, Document.class, COLLECTION_REVIEW);
    }

    public void deleteReview(String id) {
        Criteria criteria = Criteria.where(FIELD_ID).is(id);
        Query query = Query.query(criteria);

        Update updateOps = new Update()
            .set("isActive", false);

        mongoTemplate.updateFirst(query, updateOps, Document.class, COLLECTION_REVIEW);
    }

    /* 
    db.review.aggregate([
    {
        $match: {
            isActive: true
        }
    },
    {
        $group: {
            _id: "$toiletName",
            average: { $avg: "$rating" },
            count: { $sum: 1}
        }
    }
    ])  
    */
    public List<Document> getAverageRatingForAllToilets() {
        Criteria criteria = Criteria
            .where(FIELD_ISACTIVE).is(true);

        MatchOperation matchToiletNameAndIsActive = Aggregation.match(criteria);

        GroupOperation groupByToiletName = Aggregation.group("toiletName").count().as("count").avg(FIELD_RATING).as("average");

        Aggregation pipeline = Aggregation.newAggregation(matchToiletNameAndIsActive, groupByToiletName);

        AggregationResults<Document> results = mongoTemplate.aggregate(pipeline, COLLECTION_REVIEW, Document.class);
        
        return results.getMappedResults();
    }

    /* 
    db.review.aggregate([
    {
        $match: {
            toiletName: "NUS School of Computing",
            isActive: true
        }
    },
    {
        $group: {
            _id: null,
            average: { $avg: "$rating" },
            count: { $sum: 1}
        }
    }
    ])  
    */
    public List<Document> getAverageRatingByToiletName(String toiletName) {
        Criteria criteria = Criteria
            .where(FIELD_TOILETNAME).is(toiletName)
            .and(FIELD_ISACTIVE).is(true);

        MatchOperation matchToiletNameAndIsActive = Aggregation.match(criteria);

        GroupOperation groupByNull = Aggregation.group().count().as("count").avg(FIELD_RATING).as("average");

        Aggregation pipeline = Aggregation.newAggregation(matchToiletNameAndIsActive, groupByNull);

        AggregationResults<Document> results = mongoTemplate.aggregate(pipeline, COLLECTION_REVIEW, Document.class);
        
        return results.getMappedResults();
    }
    
    
}
