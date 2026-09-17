package com.coffeewa.coffeewebapp.jobapplications;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zip;

    private String positionApplying;
    private String employmentType;
    private String desiredStartDate;
    private Integer hoursAvailable;

    private String hasPreviousExperience;

    private String previousEmployer;
    private String previousJobTitle;
    private String previousStartDate;
    private String previousEndDate;
    private String reasonForLeaving;

    private String additionalExperience;

    @Column(columnDefinition = "TEXT")
    private String whyJoin;

    @Column(columnDefinition = "TEXT")
    private String aboutYourself;

    private String howDidYouHear;

    private String resumeFileName;
}
