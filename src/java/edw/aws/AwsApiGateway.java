package edw.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudfront.AmazonCloudFront;
import com.amazonaws.services.cloudfront.AmazonCloudFrontClientBuilder;
import com.amazonaws.services.cloudtrail.AWSCloudTrail;
import com.amazonaws.services.cloudtrail.AWSCloudTrailClientBuilder;
import com.amazonaws.services.config.AmazonConfig;
import com.amazonaws.services.config.AmazonConfigClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClientBuilder;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class AwsApiGateway {
    private static final Map<String, Object> regionProfileToService =
            new HashMap<>();

    private static String DEFAULT_PROFILE_NAME = "default";
    private static String DEFAULT_REGION = "us-east-1";

    private static String getRegionName(String region) {
        return region == null ? DEFAULT_REGION : region;
    }
    private static String getProfileName(String profileName) {
        return profileName == null ? DEFAULT_PROFILE_NAME : profileName;
    }
    private static AWSCredentialsProvider getAWSCredentialsProvider(String profileName) {
        return new ProfileCredentialsProvider(getProfileName(profileName));
    }

    public static synchronized void clearCache() {
        regionProfileToService.clear();
    }
    private static String toCacheKey(String region, String profileName, String className) {
        return getRegionName(region) + "|" + getProfileName(profileName) + "|" + className;
    }
    private static <T> T getCachedService(String region, String profileName, Class<T> clazz) {
        return (T)regionProfileToService.get(toCacheKey(region, profileName, clazz.getName()));
    }

    public static synchronized AmazonDynamoDB getAmazonDynamoDB(String region, String profileName) {
        AmazonDynamoDB service = getCachedService(region, profileName, AmazonDynamoDB.class);
        if (service != null) {
            return service;
        }

        service = AmazonDynamoDBClientBuilder.standard()
                .withRegion(getRegionName(region))
                .withCredentials(getAWSCredentialsProvider(profileName))
                .build();

        regionProfileToService.put(toCacheKey(region, profileName, AmazonDynamoDB.class.getName()), service);
        return service;
    }

    public static synchronized AmazonConfig getAmazonConfig(String region, String profileName) {
        AmazonConfig service = getCachedService(region, profileName, AmazonConfig.class);
        if (service != null) {
            return service;
        }

        service = AmazonConfigClientBuilder.standard()
                .withRegion(getRegionName(region))
                .withCredentials(getAWSCredentialsProvider(profileName))
                .build();

        regionProfileToService.put(toCacheKey(region, profileName, AmazonConfig.class.getName()), service);
        return service;
    }

    public static synchronized AWSCloudTrail getAWSCloudTrail(String region, String profileName) {
        AWSCloudTrail service = getCachedService(region, profileName, AWSCloudTrail.class);
        if (service != null) {
            return service;
        }

        service = AWSCloudTrailClientBuilder.standard()
                .withRegion(getRegionName(region))
                .withCredentials(getAWSCredentialsProvider(profileName))
                .build();

        regionProfileToService.put(toCacheKey(region, profileName, AWSCloudTrail.class.getName()), service);
        return service;
    }

    public static synchronized AmazonS3 getAmazonS3(String region, String profileName) {
        AmazonS3 service = getCachedService(region, profileName, AmazonS3.class);
        if (service != null) {
            return service;
        }

        service = AmazonS3ClientBuilder.standard()
                .withRegion(getRegionName(region))
                .withCredentials(getAWSCredentialsProvider(profileName))
                .build();

        regionProfileToService.put(toCacheKey(region, profileName, AmazonS3.class.getName()), service);
        return service;
    }

    public static synchronized AmazonElasticMapReduce getAmazonElasticMapReduce(String region, String profileName) {
        AmazonElasticMapReduce service = getCachedService(region, profileName, AmazonElasticMapReduce.class);
        if (service != null) {
            return service;
        }

        service = AmazonElasticMapReduceClientBuilder.standard()
                .withRegion(getRegionName(region))
                .withCredentials(getAWSCredentialsProvider(profileName))
                .build();

        regionProfileToService.put(toCacheKey(region, profileName, AmazonElasticMapReduce.class.getName()), service);
        return service;
    }

    public static synchronized Map<String, AmazonS3> getAllAmazonS3s(String profileName) {
        Map<String, AmazonS3> services = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (Regions regions: Regions.values()) {
            Region region = Region.getRegion(regions);
            if (region.isServiceSupported(AmazonS3.ENDPOINT_PREFIX)) {
                String regionName = region.getName();
                services.put(regionName, getAmazonS3(regionName, profileName));
            }
        }
        return services;
    }

    public static synchronized AmazonIdentityManagement getAmazonIdentityManagement(String region, String profileName) {
        AmazonIdentityManagement service = getCachedService(region, profileName, AmazonIdentityManagement.class);
        if (service != null) {
            return service;
        }

        service = AmazonIdentityManagementClientBuilder.standard()
                .withRegion(getRegionName(region))
                .withCredentials(getAWSCredentialsProvider(profileName))
                .build();

        regionProfileToService.put(toCacheKey(region, profileName, AmazonIdentityManagement.class.getName()), service);
        return service;
    }

    public static synchronized AmazonCloudFront getAmazonCloudFront(String region, String profileName) {
        AmazonCloudFront service = getCachedService(region, profileName, AmazonCloudFront.class);
        if (service != null) {
            return service;
        }

        service = AmazonCloudFrontClientBuilder.standard()
                .withRegion(getRegionName(region))
                .withCredentials(getAWSCredentialsProvider(profileName))
                .build();

        regionProfileToService.put(toCacheKey(region, profileName, AmazonCloudFront.class.getName()), service);
        return service;
    }
}
