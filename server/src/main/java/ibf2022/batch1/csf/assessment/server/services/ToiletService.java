package ibf2022.batch1.csf.assessment.server.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibf2022.batch1.csf.assessment.server.models.ReviewSummaryDto;
import ibf2022.batch1.csf.assessment.server.models.Toilet;
import ibf2022.batch1.csf.assessment.server.models.ToiletDto;
import ibf2022.batch1.csf.assessment.server.repositories.ToiletRepository;


@Service
public class ToiletService {

    @Autowired
    private ToiletRepository toiletRepository;

    @Autowired
    private ReviewService reviewService;

    public List<ToiletDto> getAllToilets() {
        List<Toilet> toiletList = toiletRepository.getAllToilets();

        List<Document> documentList = reviewService.getAverageRatingForAllToilets();
        Map<String, Document> stringDocumentMap = new HashMap<>();

        documentList.forEach(document -> stringDocumentMap.put(document.getString("_id"), document));

        return toiletList.stream()
            .map(toilet -> {
                ToiletDto toiletDto = new ToiletDto(toilet.getId(), toilet.getName(), toilet.getAddress(), toilet.getLatitude(), toilet.getLongitude(), new ReviewSummaryDto());
                String toiletName = toilet.getName();
                if (stringDocumentMap.containsKey(toiletName)) {
                    Document document = stringDocumentMap.get(toiletName);
                    toiletDto.getReviewSummaryDto().setAverage(document.getDouble("average"));
                    toiletDto.getReviewSummaryDto().setCount(document.getInteger("count"));
                    return toiletDto;
                }
                toiletDto.getReviewSummaryDto().setAverage(-1);
                toiletDto.getReviewSummaryDto().setCount(0);
                return toiletDto;
            })
            .toList();

    }

    public ToiletDto getToiletByName(String name) {

        Toilet toilet = toiletRepository.getToiletByName(name).orElseThrow(null);
        List<Document> documentList = reviewService.getAverageRatingByToiletName(name);
        ToiletDto toiletDto = new ToiletDto(toilet.getId(), toilet.getName(), toilet.getAddress(), toilet.getLatitude(), toilet.getLongitude(), new ReviewSummaryDto());
        if (!documentList.isEmpty()) {
            Document document = documentList.get(0);
            toiletDto.getReviewSummaryDto().setAverage(document.getDouble("average"));
            toiletDto.getReviewSummaryDto().setCount(document.getInteger("count"));
            return toiletDto;
        }

        toiletDto.getReviewSummaryDto().setAverage(-1);
        toiletDto.getReviewSummaryDto().setCount(0);
        return toiletDto;
    }
    
}
