package ibf2022.batch1.csf.assessment.server.models;

public class ToiletDto {

    private Integer id;

    private String name;

    private String address;

    private String latitude;

    private String longitude;

    private ReviewSummaryDto reviewSummaryDto;

    public ToiletDto() {
    } 

    public ToiletDto(Integer id, String name, String address, String latitude, String longitude, ReviewSummaryDto reviewSummaryDto) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.reviewSummaryDto = reviewSummaryDto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public ReviewSummaryDto getReviewSummaryDto() {
        return reviewSummaryDto;
    }

    public void setReviewSummaryDto(ReviewSummaryDto reviewSummaryDto) {
        this.reviewSummaryDto = reviewSummaryDto;
    }

    

    
    
}
