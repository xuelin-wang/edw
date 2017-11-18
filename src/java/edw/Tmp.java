package edw;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.Grant;
import edw.aws.AwsApiGateway;

import java.util.List;
import java.util.Map;

public class Tmp {
    public static void main(String[] args) {

//        AmazonDynamoDB ddb = AwsApiGateway.getAmazonDynamoDB(null);
//
//        ListTablesResult res = ddb.listTables();
//
//        AmazonConfig amazonConfig = AwsApiGateway.getAmazonConfig(null);
//
//        DescribeDeliveryChannelStatusResult result = amazonConfig.describeDeliveryChannelStatus();

//        AWSCloudTrail cloudTrail = AwsApiGateway.getAWSCloudTrailClient(null);
//        DescribeTrailsResult trailsRes = cloudTrail.describeTrails();
//        GetTrailStatusRequest req = new GetTrailStatusRequest();
//        for (Trail trail: trailsRes.getTrailList()) {
//            req.setName(trail.getTrailARN());
//            GetTrailStatusResult res = cloudTrail.getTrailStatus(req);
//            boolean isLogging = res.isLogging();
//        }

        Map<String, AmazonS3> servicesMap = AwsApiGateway.getAllAmazonS3s(null);
        AmazonS3 service = servicesMap.entrySet().iterator().next().getValue();

        List<Bucket> buckets = service.listBuckets();
        for (Bucket bucket: buckets) {
            String bucketName = bucket.getName();

            String location = service.getBucketLocation(bucketName);
            if ("US".equalsIgnoreCase(location)) {
                location = "us-east-1";
            }
            else if ("EU".equalsIgnoreCase(location)) {
                location = "eu-west-1";
            }

            AmazonS3 regionService = servicesMap.get(location);

            System.out.println("bucket: " + bucketName + ", location: " + location);
            AccessControlList acl = regionService.getBucketAcl(bucket.getName());

            List<Grant> grants = acl.getGrantsAsList();

            System.out.println("there");
        }


        System.out.println("here");
    }
}
