package com.kenaxisq.nestnavigate.property.entity;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(name="uuid", strategy = "uuid")
    private String id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String propertyCategory;
    @Column(nullable = false)
    private String facing;
    @Column(nullable = false)
    private String propertyListingFor;
    @Column(nullable = true)
    private String projectName;
    @Column(nullable = false)
    private String furnitureStatus;
    @Column(nullable= true)
    private String furnitureStatusDescription;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Double super_builtup_area;
    @Column(nullable = true)
    private Double carpet_area;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = true)
    private Double Advance;
    @Column
    private Double length;
    @Column
    private Double width;
    @Column(nullable = true)
    private Integer poojaRoom;
    @Column(nullable = true)
    private Integer noOfBedrooms;
    @Column(nullable = false)
    private Boolean isNegotiable;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User Owner;
    @Column(nullable = false)
    private String status = PropertyStatus.AVAILABLE.name();
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
    private String listedby;
    @Column(nullable = false)
    private String primaryContact;
    @Column
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
    @Column(nullable = true)
    private String longitude;
    @Column(nullable = true)
    private String latitude;
    @Column(nullable = true)
    private int views=0;
    @Column(nullable = false)
    private int likes=0;
    @Column
    private String Media;
    @Column(nullable = false)
    private LocalDateTime moveInDate;

    public void incrementViewsOfAProperty() {
       setViews(getViews()+1);
    }
    public void incrementLikesOfAProperty() {
        setLikes(getLikes()+1);
    }
}

