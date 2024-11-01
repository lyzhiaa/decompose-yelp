package kh.edu.istad.business.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long businessId;
    private String reviewerName;

    // Text content of the review
    private String reviewContent;
    private Double rating;
    private Integer likeCount;
    private Boolean isHelpful;

}
