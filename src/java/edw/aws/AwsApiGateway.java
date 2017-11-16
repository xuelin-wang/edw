package edw.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.config.AmazonConfig;
import com.amazonaws.services.config.AmazonConfigClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

public class AwsApiGateway {
    private static String DEFAULT_PROFILE_NAME = "default";

    private static AWSCredentialsProvider getAWSCredentialsProvider(String profileName) {
        return new ProfileCredentialsProvider(profileName == null ? DEFAULT_PROFILE_NAME : profileName);
    }

    public static AmazonDynamoDB getAmazonDynamoDB(String profileName) {
        AmazonDynamoDB service = AmazonDynamoDBClientBuilder.standard()
//                .withRegion(Regions.US_WEST_2)
                .withCredentials(getAWSCredentialsProvider(profileName))
                .build();
        return service;
    }

    public static AmazonConfig getAmazonConfig(String profileName) {
        AmazonConfig service = AmazonConfigClientBuilder.standard()
                .withCredentials(getAWSCredentialsProvider(profileName))
                .withCredentials(getAWSCredentialsProvider(profileName))
                .build();
        return service;
    }
}
