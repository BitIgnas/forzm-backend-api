package org.forzm.demo.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import io.awspring.cloud.ses.SimpleEmailServiceJavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class AwsMailConfig {

//    @Value("${cloud.aws.credentials.access-key}")
//    private String awsId;
//
//    @Value("${cloud.aws.credentials.secret-key}")
//    private String awsKey;
//
//    @Value("${cloud.aws.region.static}")
//    private String awsRegion;
//
//    @Bean
//    public AmazonSimpleEmailService amazonSimpleEmailService() {
//        return AmazonSimpleEmailServiceClientBuilder.standard()
//                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsId, awsKey)))
//                .withRegion(awsRegion)
//                .build();
//    }
//
//    @Bean
//    public JavaMailSender javaMailSender(AmazonSimpleEmailService amazonSimpleEmailService) {
//        return new SimpleEmailServiceJavaMailSender(amazonSimpleEmailService);
//    }

}
