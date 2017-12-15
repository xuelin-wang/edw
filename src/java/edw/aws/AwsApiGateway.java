package edw.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider;
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
import com.amazonaws.services.elasticsearch.AWSElasticsearch;
import com.amazonaws.services.elasticsearch.AWSElasticsearchClientBuilder;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.AmazonRDSClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.util.json.Jackson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class AwsApiGateway {
    public static final String EXTERNAL_ID = "xuelinwangdevinsecure";
    public static final String ROLE_ARN = "arn:aws:iam::551164845140:role/xuelinwang";

    private static final Map<String, Object> regionProfileToService =
            new HashMap<>();

    private static String getRegionName(String region) {
        return region == null ? "us-east-2" : region;
    }
    private static String getProfileName(String profileName) {
        return profileName == null ? "default" : profileName;
    }
    private static AWSCredentialsProvider getAWSCredentialsProvider(String profileName) {
        return new ProfileCredentialsProvider(getProfileName(profileName));
    }

    private static STSAssumeRoleSessionCredentialsProvider getSTSAssumeRoleSessionCredentialsProvider( String externalId, String roleARN) {
        STSAssumeRoleSessionCredentialsProvider.Builder builder =
                new STSAssumeRoleSessionCredentialsProvider.Builder(roleARN, externalId + "_" + System.currentTimeMillis());
        builder.withExternalId(externalId)
                .withRoleSessionDurationSeconds(3600);
        return builder.build();
    }

    public static synchronized void clearCache() {
        regionProfileToService.clear();
    }
    private static String toCacheKey(String region, String profileName, String externalId, String className) {
        return getRegionName(region) + "|" + getProfileName(profileName) + (externalId == null ? "" : externalId) + "|" + className;
    }

    @SuppressWarnings("unchecked")
    private static <T> T getCachedService(String region, String profileName, String externalId, Class<T> clazz) {
        return (T)regionProfileToService.get(toCacheKey(region, profileName, externalId, clazz.getName()));
    }

    private static <T> void putCachedService(String region, String profileName, String externalId, Class<T> clazz, T service) {
        regionProfileToService.put(toCacheKey(region, profileName, externalId, clazz.getName()), service);
    }

    public static String toJsonPrettyString(Object awsObj) {
        return Jackson.toJsonPrettyString(awsObj);
    }

    public static String toJsonString(Object awsObj) {
        return Jackson.toJsonString(awsObj);
    }

    public static synchronized <T> T getAWSService(String region, String profileName, String externalId, String roleARN, Class<T> clazz)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        {
            T service = getCachedService(region, profileName, externalId, clazz);
            if (service != null) {
                return service;
            }
        }

        Class<?> builderClass = Class.forName(clazz.getName() + "ClientBuilder");
        Method method = builderClass.getMethod("standard");
        Object obj = method.invoke(null);
        method = builderClass.getMethod("withRegion", String.class);
        method.invoke(obj, getRegionName(region));
        method = builderClass.getMethod("withCredentials", AWSCredentialsProvider.class);
        AWSCredentialsProvider awsCredentialsProvider;
        if (externalId == null) {
            awsCredentialsProvider = getAWSCredentialsProvider(profileName);
        }
        else {
            awsCredentialsProvider = getSTSAssumeRoleSessionCredentialsProvider(externalId, roleARN);
        }
        method.invoke(obj, awsCredentialsProvider);
        method = builderClass.getMethod("build");
        @SuppressWarnings("unchecked")
        T service = (T)method.invoke(obj);
        putCachedService(region, profileName, externalId, clazz, service);
        return service;
    }

//    public static synchronized AmazonDynamoDB getAmazonDynamoDB(String region, String profileName) {
//        AmazonDynamoDB service = getCachedService(region, profileName, AmazonDynamoDB.class);
//        if (service != null) {
//            return service;
//        }
//
//        service = AmazonDynamoDBClientBuilder.standard()
//                .withRegion(getRegionName(region))
//                .withCredentials(getAWSCredentialsProvider(profileName))
//                .build();
//
//        putCachedService(region, profileName, AmazonDynamoDB.class, service);
//        return service;
//    }
//
//    public static synchronized AmazonConfig getAmazonConfig(String region, String profileName) {
//        AmazonConfig service = getCachedService(region, profileName, AmazonConfig.class);
//        if (service != null) {
//            return service;
//        }
//
//        service = AmazonConfigClientBuilder.standard()
//                .withRegion(getRegionName(region))
//                .withCredentials(getAWSCredentialsProvider(profileName))
//                .build();
//
//        putCachedService(region, profileName, AmazonConfig.class, service);
//        return service;
//    }
//
//    public static synchronized AWSCloudTrail getAWSCloudTrail(String region, String profileName) {
//        AWSCloudTrail service = getCachedService(region, profileName, AWSCloudTrail.class);
//        if (service != null) {
//            return service;
//        }
//
//        service = AWSCloudTrailClientBuilder.standard()
//                .withRegion(getRegionName(region))
//                .withCredentials(getAWSCredentialsProvider(profileName))
//                .build();
//
//        putCachedService(region, profileName, AWSCloudTrail.class, service);
//        return service;
//    }
//
//    public static synchronized AmazonS3 getAmazonS3(String region, String profileName) {
//        AmazonS3 service = getCachedService(region, profileName, AmazonS3.class);
//        if (service != null) {
//            return service;
//        }
//
//        service = AmazonS3ClientBuilder.standard()
//                .withRegion(getRegionName(region))
//                .withCredentials(getAWSCredentialsProvider(profileName))
//                .build();
//
//        putCachedService(region, profileName, AmazonS3.class, service);
//        return service;
//    }
//
//    public static synchronized AmazonElasticMapReduce getAmazonElasticMapReduce(String region, String profileName) {
//        AmazonElasticMapReduce service = getCachedService(region, profileName, AmazonElasticMapReduce.class);
//        if (service != null) {
//            return service;
//        }
//
//        service = AmazonElasticMapReduceClientBuilder.standard()
//                .withRegion(getRegionName(region))
//                .withCredentials(getAWSCredentialsProvider(profileName))
//                .build();
//
//        putCachedService(region, profileName, AmazonElasticMapReduce.class, service);
//        return service;
//    }
//
//    public static synchronized Map<String, AmazonS3> getAllAmazonS3s(String profileName) {
//        Map<String, AmazonS3> services = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
//        for (Regions regions: Regions.values()) {
//            Region region = Region.getRegion(regions);
//            if (region.isServiceSupported(AmazonS3.ENDPOINT_PREFIX)) {
//                String regionName = region.getName();
//                services.put(regionName, getAmazonS3(regionName, profileName));
//            }
//        }
//        return services;
//    }
//
//    public static synchronized AmazonIdentityManagement getAmazonIdentityManagement(String region, String profileName) {
//        AmazonIdentityManagement service = getCachedService(region, profileName, AmazonIdentityManagement.class);
//        if (service != null) {
//            return service;
//        }
//
//        service = AmazonIdentityManagementClientBuilder.standard()
//                .withRegion(getRegionName(region))
//                .withCredentials(getAWSCredentialsProvider(profileName))
//                .build();
//
//        putCachedService(region, profileName, AmazonIdentityManagement.class, service);
//        return service;
//    }
//
//    public static synchronized AmazonCloudFront getAmazonCloudFront(String region, String profileName) {
//        AmazonCloudFront service = getCachedService(region, profileName, AmazonCloudFront.class);
//        if (service != null) {
//            return service;
//        }
//
//        service = AmazonCloudFrontClientBuilder.standard()
//                .withRegion(getRegionName(region))
//                .withCredentials(getAWSCredentialsProvider(profileName))
//                .build();
//
//        putCachedService(region, profileName, AmazonCloudFront.class, service);
//        return service;
//    }
//
//    public static synchronized AWSLambda getAWSLambda(String region, String profileName) {
//        AWSLambda service = getCachedService(region, profileName, AWSLambda.class);
//        if (service != null) {
//            return service;
//        }
//
//        service = AWSLambdaClientBuilder.standard()
//                .withRegion(getRegionName(region))
//                .withCredentials(getAWSCredentialsProvider(profileName))
//                .build();
//
//        putCachedService(region, profileName, AWSLambda.class, service);
//        return service;
//    }
//
//    public static synchronized AmazonRDS getAmazonRDS(String region, String profileName) {
//        AmazonRDS service = getCachedService("", profileName, AmazonRDS.class);
//        if (service != null) {
//            return service;
//        }
//
//        service = AmazonRDSClientBuilder.standard()
//                .withRegion(getRegionName(region))
//                .withCredentials(getAWSCredentialsProvider(profileName))
//                .build();
//
//        putCachedService(region, profileName, AmazonRDS.class, service);
//        return service;
//    }
//
//    public static synchronized AWSElasticsearch getAWSElasticsearch(String region, String profileName) {
//        AWSElasticsearch service = getCachedService("", profileName, AWSElasticsearch.class);
//        if (service != null) {
//            return service;
//        }
//
//        service = AWSElasticsearchClientBuilder.standard()
//                .withRegion(getRegionName(region))
//                .withCredentials(getAWSCredentialsProvider(profileName))
//                .build();
//
//        putCachedService(region, profileName, AWSElasticsearch.class, service);
//        return service;
//    }
//
//    public static synchronized AWSSecurityTokenService getAAWSSecurityTokenServiceh(String region, String profileName) {
//        AWSSecurityTokenService service = getCachedService("", profileName, AWSSecurityTokenService.class);
//        if (service != null) {
//            return service;
//        }
//
//        service = AWSSecurityTokenServiceClientBuilder.standard()
//                .withRegion(getRegionName(region))
//                .withCredentials(getAWSCredentialsProvider(profileName))
//                .build();
//
//        putCachedService(region, profileName, AWSSecurityTokenService.class, service);
//        return service;
//    }
}
