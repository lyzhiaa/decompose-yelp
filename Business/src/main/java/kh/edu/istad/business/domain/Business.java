package kh.edu.istad.business.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import javax.xml.stream.Location;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "business")
@Entity
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String alias;

    private String name;

    private String imageUrl;

    private Boolean isClosed;

    private String url;

    private Integer reviewCount;

    private String rating;

    @ManyToMany
    private List<Category> categories;

    private String price;

    private String phone;

    private String displayPhone;

    private String distance;

    private Integer photosCount;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Location location;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<OpeningHour> specialHours;

    private Boolean isClaimed;

    private String dateOpened;

    private String dateClosed;

    private String businessType;

    @ManyToMany
    @JoinTable(
            name = "business_transaction",
            joinColumns = @JoinColumn(name = "business_id"),
            inverseJoinColumns = @JoinColumn(name = "transaction_id"))
    private List<Transaction> transaction;

    private Boolean isOpenNow;

}
