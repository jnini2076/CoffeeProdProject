package com.coffeewa.coffeewebapp.jobapplications;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\d{10}$", message = "phone must contain exactly 10 digits")
    private String phone;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    @Pattern(regexp = "^\\d{5}$", message = "ZIP must be exactly 5 digits")
    private String zip;

    @NotBlank
    private String positionApplying;

    @NotBlank
    private String employmentType;

    @NotBlank
    private String desiredStartDate;

    @NotNull
    @Min(1)
    @Max(40)
    private Integer hoursAvailable;

    @NotBlank
    private String hasPreviousExperience;

    private String previousEmployer;
    private String previousJobTitle;
    private String previousStartDate;
    private String previousEndDate;
    private String reasonForLeaving;

    private String additionalExperience;

    @NotBlank
    @Size(min = 50, message = "must be at least 50 characters")
    private String whyJoin;

    @NotBlank
    @Size(min = 50, message = "must be at least 50 characters")
    private String aboutYourself;

    @NotBlank
    private String howDidYouHear;

    @NotBlank
    private String resumeFileName;

    @NotBlank
    private String resumeBase64;
}
