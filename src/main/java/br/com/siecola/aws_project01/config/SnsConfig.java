package br.com.siecola.aws_project01.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.Topic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!local")
public class SnsConfig {

    @Value("${aws.region}")
    private String region;

    @Value("${aws.sns.topic.products.event.arn}")
    private String arn;

    @Bean
    public AmazonSNS snsClient() {
        return AmazonSNSClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();
    }


    @Bean(name = "productEventsTopic")
    public Topic snsProductEventsTopic(){
        return new Topic().withTopicArn(arn);
    }

}
