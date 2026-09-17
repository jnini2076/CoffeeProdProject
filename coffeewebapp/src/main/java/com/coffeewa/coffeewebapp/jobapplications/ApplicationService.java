package com.coffeewa.coffeewebapp.jobapplications;

import java.util.Base64;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.coffeewa.coffeewebapp.service.SESservice;

import jakarta.transaction.Transactional;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class ApplicationService {

    private static final String BUCKET = "coffeeapplications3";

    private final Logger logger = LogManager.getLogger(ApplicationService.class);

    private final JobApplicationRepo jobApplicationRepo;
    private final S3Client s3Client;

    public ApplicationService(JobApplicationRepo jobApplicationRepo, S3Client s3Client) {
        this.jobApplicationRepo = jobApplicationRepo;
        this.s3Client = s3Client;
    }

    @Transactional
    public void saveApplication(ApplicationDTO dto) {

        byte[] fileBytes = Base64.getDecoder().decode(dto.getResumeBase64());
        String s3Key = "applications/" + UUID.randomUUID() + "/" + dto.getResumeFileName();

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(s3Key)
                .build();

        s3Client.putObject(putRequest, RequestBody.fromBytes(fileBytes));
        logger.info("Resume uploaded to S3");

        JobApplication application = JobApplication.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .city(dto.getCity())
                .state(dto.getState())
                .zip(dto.getZip())
                .positionApplying(dto.getPositionApplying())
                .employmentType(dto.getEmploymentType())
                .desiredStartDate(dto.getDesiredStartDate())
                .hoursAvailable(dto.getHoursAvailable())
                .hasPreviousExperience(dto.getHasPreviousExperience())
                .previousEmployer(dto.getPreviousEmployer())
                .previousJobTitle(dto.getPreviousJobTitle())
                .previousStartDate(dto.getPreviousStartDate())
                .previousEndDate(dto.getPreviousEndDate())
                .reasonForLeaving(dto.getReasonForLeaving())
                .additionalExperience(dto.getAdditionalExperience())
                .whyJoin(dto.getWhyJoin())
                .aboutYourself(dto.getAboutYourself())
                .howDidYouHear(dto.getHowDidYouHear())
                .resumeFileName(s3Key)
                .build();

        jobApplicationRepo.save(application);
        logger.info("Job application saved");
    }
}
