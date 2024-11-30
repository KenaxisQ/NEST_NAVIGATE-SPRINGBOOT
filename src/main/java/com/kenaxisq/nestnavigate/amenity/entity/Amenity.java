package com.kenaxisq.nestnavigate.amenity.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "AMENITY")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Amenity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private String subCategory;


    //    private String description;
    public Amenity(String name, String category) {
        this.name = name;
        this.category = category;
    }

}
