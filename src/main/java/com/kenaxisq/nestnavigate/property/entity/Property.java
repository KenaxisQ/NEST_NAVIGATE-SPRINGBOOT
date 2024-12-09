package com.kenaxisq.nestnavigate.property.entity;

import com.kenaxisq.nestnavigate.utils.property.Directions;
import com.kenaxisq.nestnavigate.utils.property.Furniture;
import com.kenaxisq.nestnavigate.utils.property.PropertyStatus;
import com.kenaxisq.nestnavigate.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "PROPERTY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Property {
    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    @GenericGenerator(name="uuid", strategy = "uuid")
    private String id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String propertyCategory;
    @Column(nullable = true)
    private Directions facing;
    @Column(nullable = false)
    private String propertyListingFor;
    @Column(nullable = false)
    private String projectName;
    @Column(nullable = true)
    private Furniture furnitureStatus;
    @Column(nullable= true)
    private String furnitureStatusDescription;
    @Column(nullable = false)
    private String description;
    @Column(nullable = true)
    private Double superBuiltupArea;
    @Column(nullable = true)
    private Double carpetArea;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private Double advance;
    @Column(nullable = true)
    private Double length;
    @Column(nullable = true)
    private Double width;
    @Column(nullable = true)
    private Integer poojaRoom;
    @Column(nullable = true)
    private Integer noOfBedrooms;
    @Column(nullable = true)
    private Integer noOfBathrooms;
    @Column(nullable = true)
    private Integer noOfRooms;
    @Column(nullable = true)
    private Integer noOfBalconies;
    @Column(nullable = false)
    private Boolean isNegotiable;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;
    @Column(nullable = false)
    private PropertyStatus status = PropertyStatus.AVAILABLE;
    @Column(nullable = false)
    private Boolean isFeatured = false;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime listedDate =LocalDateTime.now();;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedDate = LocalDateTime.now();;
    @Column(nullable = false)
    private LocalDateTime expiryDate = listedDate.plus(30, ChronoUnit.DAYS);
    @Column(nullable = false)
    private String listedby ;
    @Column(nullable = false)
    private String primaryContact;
    @Column(nullable = true)
    private String secondaryContact;
    @Column(nullable = false)
    private String state ="Andhra Pradesh";
    @Column(nullable = false)
    private String country ="India";
    @Column(nullable = false)
    private String revenueDivision="Srikakulam";
    @Column(nullable = false)
    private String mandal;
    @Column(nullable = false)
    private String village;
    @Column(nullable = false)
    private String zip;
    @Column(nullable = false)
    private String address;
    @Column(nullable = true)
    private String longitude;
    @Column(nullable = true)
    private String latitude;
    @Column(nullable = true)
    private Integer views=0;
    @Column(nullable = true)
    private Integer likes=0;
    @Column(nullable = true)
    private String media;
    @Column(nullable = true)
    private LocalDateTime moveInDate;
    @Column(nullable = true)
    private String amenities;
    @Column(nullable = false)
    private String propertyApprovalStatus;

    public void initializeApprovalStatus(String role) {
        switch (role) {
            case "ADMIN":
                this.propertyApprovalStatus = "Approved";
                break;
            default:
                this.propertyApprovalStatus = "Awaiting Approval";
                break;
        }
    }
    public void incrementViewsOfAProperty() {
       setViews(getViews()+1);
    }
    public void incrementLikesOfAProperty() {
        setLikes(getLikes()+1);
    }
}

