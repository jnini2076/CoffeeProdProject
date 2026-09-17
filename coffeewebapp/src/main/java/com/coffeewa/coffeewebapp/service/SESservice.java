package com.coffeewa.coffeewebapp.service;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.coffeewa.coffeewebapp.jobapplications.ApplicationDTO;

import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

@Service
public class SESservice {

private final String domain;
private final SesClient sesClient;
private final Logger logger = LogManager.getLogger(SESservice.class);

        public SESservice(SesClient sesClient, @Value("${ses.domain}") String domain) {
            this.sesClient = sesClient;
            this.domain = domain;
        }


        @Async
        public void GenerateEmail(ApplicationDTO applicationDTO){
                    String subject = "Jonathan's CoffeeShop Application";
                    String from = "no-reply@" + domain;
                    logger.info("what is this" + domain);
                    SendEmailRequest emailRequest = SendEmailRequest.builder()
                     .source(from)
                .destination(Destination.builder().toAddresses(applicationDTO.getEmail()).build())
                .message(Message.builder()
                        .subject(Content.builder().data(subject).charset("UTF-8").build())
                        .body(Body.builder()
                                .html(Content.builder().data(EmailDesign()).charset("UTF-8").build())
                                .build())
                        .build())
                .build();
            logger.info("generated email");
        sesClient.sendEmail(emailRequest);
    }

        public String EmailDesign(){


            String htmlbody = "<html> <h3>Thank you for trying out my web application. Please do not reply back </h3>  </html> ";
            return htmlbody;
        }



}
