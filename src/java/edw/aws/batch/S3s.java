package edw.aws.batch;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.Grant;
import com.amazonaws.services.s3.model.Grantee;
import com.amazonaws.services.s3.model.ListBucketsRequest;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.waiters.AmazonS3Waiters;
import com.amazonaws.waiters.Waiter;
import com.amazonaws.waiters.WaiterParameters;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class S3s {
    private static final Logger logger = Logger.getLogger(S3s.class.getName());

    private static boolean isBucketNameValid(String name) {
        return !Strings.isNullOrEmpty(name) &&
                name.chars().allMatch( (int ii) -> {
                    char ch = (char)ii;
                    return ch >= '0' && ch <= '9' ||
                            ch >= 'a' && ch <= 'z' ||
                            ch == '-' ||
                            ch == '.';
                }) &&
                name.charAt(name.length() - 1) != '-' &&
                name.indexOf("..") < 0 &&
                name.length() >= 1 && name.length() <= 63 &&
                name.indexOf(".-") < 0 &&
                name.indexOf("-.") < 0;
    }

    public static Bucket createBucket(String name, String region,
                                      Grant[] grants,
                                      String tagKey, String tagVal, AmazonS3 s3) {
        if (!isBucketNameValid(name)) {
            throw new RuntimeException("Invalid bucket name:: name=" + name);
        }

        CreateBucketRequest createBucketRequest = new CreateBucketRequest(name, region);
        if (grants != null) {
            AccessControlList accessControlList = new AccessControlList();
            accessControlList.grantAllPermissions(grants);
            createBucketRequest.setAccessControlList(accessControlList);
        }
        return s3.createBucket(createBucketRequest);
    }

    public static void waitForBucket(String bucketName, AmazonS3 s3) throws InterruptedException {
        long beforeMillis = System.currentTimeMillis();
        while (!s3.doesBucketExistV2(bucketName)) {
            Thread.sleep(50);
        }
        long afterMillis = System.currentTimeMillis();
        logger.info("Waiting for buckets::bucket_name=" + bucketName + ":: wait_millis=" + (afterMillis - beforeMillis));
    }

    public static Bucket createAndWaitForBucket(String name, String region, Grant[] grants, String tagKey, String tagVal, AmazonS3 s3)
            throws InterruptedException {
        Bucket bucket = createBucket(name, region, grants, tagKey, tagVal, s3);
        waitForBucket(name, s3);
        return bucket;
    }

    private static String normalizeBucketLocation(String location) {
        if (location.equalsIgnoreCase("US")) {
            location = "us-east-1";
        } else if (location.equalsIgnoreCase("EU")) {
            location = "eu-west-1";
        }
        return location;
    }

    public static List<Bucket> findBucketsByNamePatternAndRegion(String namePattern, String region, AmazonS3 s3) {
        List<Bucket> buckets = s3.listBuckets();
        Stream<Bucket> bucketStream = buckets.parallelStream();
        if (namePattern != null) {
            Pattern pattern = Pattern.compile(namePattern);
            bucketStream = bucketStream.filter(bucket -> pattern.matcher(bucket.getName()).matches());
        }

        if (region != null) {
            bucketStream = bucketStream.filter(
                    (Bucket bucket) ->
                    {
                        String location = s3.getBucketLocation(bucket.getName());
                        location = normalizeBucketLocation(location);
                        return region.equals(location);
                    }
            );
        }

        return bucketStream.collect(Collectors.toList());
    }

    public static void deleteBuckets(String namePattern, String region, AmazonS3 s3)
    {
        List<Bucket> buckets = findBucketsByNamePatternAndRegion(namePattern, region, s3);
        for (Bucket bucket: buckets) {
            s3.deleteBucket(bucket.getName());
        }

        buckets.parallelStream().forEach(
                (Bucket bucket) -> {
                    long beforeMillis = System.currentTimeMillis();
                    while (s3.doesBucketExistV2(bucket.getName())) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            logger.severe("Error while waiting for bucket::bucketName=" + bucket.getName());
                        }
                    }
                    long waitMillis = System.currentTimeMillis() - beforeMillis;
                    logger.info("Waiting for buckets::bucket_name=" + bucket.getName() + ":: wait_millis=" + waitMillis);
                }
        );
    }
}
