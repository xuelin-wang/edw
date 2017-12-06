package edw;

import com.amazonaws.services.cloudfront.AmazonCloudFront;
import com.amazonaws.services.cloudfront.model.DistributionConfig;
import com.amazonaws.services.cloudfront.model.DistributionSummary;
import com.amazonaws.services.cloudfront.model.GetDistributionConfigRequest;
import com.amazonaws.services.cloudfront.model.GetDistributionConfigResult;
import com.amazonaws.services.cloudfront.model.ListDistributionsRequest;
import com.amazonaws.services.cloudfront.model.ListDistributionsResult;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.model.ClusterSummary;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.model.AccessKeyMetadata;
import com.amazonaws.services.identitymanagement.model.AttachedPolicy;
import com.amazonaws.services.identitymanagement.model.GetAccessKeyLastUsedRequest;
import com.amazonaws.services.identitymanagement.model.GetAccessKeyLastUsedResult;
import com.amazonaws.services.identitymanagement.model.GetPolicyRequest;
import com.amazonaws.services.identitymanagement.model.GetPolicyResult;
import com.amazonaws.services.identitymanagement.model.GetPolicyVersionRequest;
import com.amazonaws.services.identitymanagement.model.GetPolicyVersionResult;
import com.amazonaws.services.identitymanagement.model.GetUserPolicyRequest;
import com.amazonaws.services.identitymanagement.model.GetUserPolicyResult;
import com.amazonaws.services.identitymanagement.model.Group;
import com.amazonaws.services.identitymanagement.model.ListAccessKeysRequest;
import com.amazonaws.services.identitymanagement.model.ListAttachedGroupPoliciesRequest;
import com.amazonaws.services.identitymanagement.model.ListAttachedGroupPoliciesResult;
import com.amazonaws.services.identitymanagement.model.ListAttachedUserPoliciesRequest;
import com.amazonaws.services.identitymanagement.model.ListAttachedUserPoliciesResult;
import com.amazonaws.services.identitymanagement.model.ListGroupPoliciesRequest;
import com.amazonaws.services.identitymanagement.model.ListGroupPoliciesResult;
import com.amazonaws.services.identitymanagement.model.ListGroupsForUserRequest;
import com.amazonaws.services.identitymanagement.model.ListGroupsForUserResult;
import com.amazonaws.services.identitymanagement.model.ListUserPoliciesRequest;
import com.amazonaws.services.identitymanagement.model.ListUserPoliciesResult;
import com.amazonaws.services.identitymanagement.model.SimulatePrincipalPolicyRequest;
import com.amazonaws.services.identitymanagement.model.SimulatePrincipalPolicyResult;
import com.amazonaws.services.identitymanagement.model.User;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.Grant;
import edw.aws.AwsApiGateway;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Tmp {
    public static void main(String[] args) {

//        AmazonDynamoDB ddb = AwsApiGateway.getAmazonDynamoDB(null);
//
//        ListTablesResult res = ddb.listTables();
//
//        AmazonConfig amazonConfig = AwsApiGateway.getAmazonConfig(null);
//
//        DescribeDeliveryChannelStatusResult result = amazonConfig.describeDeliveryChannelStatus();

//        AWSCloudTrail cloudTrail = AwsApiGateway.getAWSCloudTrail(null);
//        DescribeTrailsResult trailsRes = cloudTrail.describeTrails();
//        GetTrailStatusRequest req = new GetTrailStatusRequest();
//        for (Trail trail: trailsRes.getTrailList()) {
//            req.setName(trail.getTrailARN());
//            GetTrailStatusResult res = cloudTrail.getTrailStatus(req);
//            boolean isLogging = res.isLogging();
//        }

//        Map<String, AmazonS3> servicesMap = AwsApiGateway.getAllAmazonS3s(null);
//        AmazonS3 service = servicesMap.entrySet().iterator().next().getValue();
//
//        List<Bucket> buckets = service.listBuckets();
//        for (Bucket bucket: buckets) {
//            String bucketName = bucket.getName();
//
//            String location = service.getBucketLocation(bucketName);
//            if ("US".equalsIgnoreCase(location)) {
//                location = "us-east-1";
//            }
//            else if ("EU".equalsIgnoreCase(location)) {
//                location = "eu-west-1";
//            }
//
//            AmazonS3 regionService = servicesMap.get(location);
//
//            System.out.println("bucket: " + bucketName + ", location: " + location);
//            AccessControlList acl = regionService.getBucketAcl(bucket.getName());
//
//            List<Grant> grants = acl.getGrantsAsList();
//
//            System.out.println("there");
//        }

//        AmazonElasticMapReduce emr = AwsApiGateway.getAmazonElasticMapReduce(null, null);
//        List<ClusterSummary> clusters = emr.listClusters().getClusters();
//        for (ClusterSummary cluster: clusters) {
//        }

//        AmazonIdentityManagement service = AwsApiGateway.getAmazonIdentityManagement(null, null);
//        List<User> users = service.listUsers().getUsers();
//        List<String> actionNames = Arrays.asList("s3:DeleteBucket");
//        List<String> resourceArns = Arrays.asList("*");
//        for (User user: users) {
//            SimulatePrincipalPolicyRequest simulatePrincipalPolicyRequest = new SimulatePrincipalPolicyRequest();
//            simulatePrincipalPolicyRequest.setActionNames(actionNames);
//            simulatePrincipalPolicyRequest.setResourceArns(resourceArns);
//            simulatePrincipalPolicyRequest.setPolicySourceArn(user.getArn());
//            SimulatePrincipalPolicyResult simulatePrincipalPolicyResult = service.simulatePrincipalPolicy(simulatePrincipalPolicyRequest);
//
//            ListUserPoliciesRequest listUserPoliciesRequest = new ListUserPoliciesRequest();
//            listUserPoliciesRequest.setUserName(user.getUserName());
//            ListUserPoliciesResult listUserPoliciesResult = service.listUserPolicies(listUserPoliciesRequest);
//
//            for (String policyName: listUserPoliciesResult.getPolicyNames()) {
//                GetUserPolicyRequest getUserPolicyRequest = new GetUserPolicyRequest();
//                getUserPolicyRequest.setUserName(user.getUserName());
//                getUserPolicyRequest.setPolicyName(policyName);
//
//                GetUserPolicyResult getUserPolicyResult = service.getUserPolicy(getUserPolicyRequest);
//                System.out.println("there");
//            }
//
//            ListAttachedUserPoliciesRequest listAttachedUserPoliciesRequest = new ListAttachedUserPoliciesRequest();
//            listAttachedUserPoliciesRequest.setUserName(user.getUserName());
//            ListAttachedUserPoliciesResult listAttachedUserPoliciesResult = service.listAttachedUserPolicies(listAttachedUserPoliciesRequest);
//            for (AttachedPolicy policy: listAttachedUserPoliciesResult.getAttachedPolicies()) {
//                String doc = getPolicyDoc(policy, service);
//                String policyName = policy.getPolicyName();
//                System.out.println("there3");
//
//            }
//
//            ListGroupsForUserRequest listGroupsForUserRequest = new ListGroupsForUserRequest();
//            listGroupsForUserRequest.setUserName(user.getUserName());
//            ListGroupsForUserResult listGroupsForUserResult = service.listGroupsForUser(listGroupsForUserRequest);
//
//            for (Group group: listGroupsForUserResult.getGroups()) {
//                ListGroupPoliciesRequest listGroupPoliciesRequest = new ListGroupPoliciesRequest();
//                listGroupPoliciesRequest.setGroupName(group.getGroupName());
//                ListGroupPoliciesResult listGroupPoliciesResult = service.listGroupPolicies(listGroupPoliciesRequest);
//                for (String policyName: listGroupPoliciesResult.getPolicyNames()) {
//                    GetUserPolicyRequest getUserPolicyRequest = new GetUserPolicyRequest();
//                    getUserPolicyRequest.setUserName(user.getUserName());
//                    getUserPolicyRequest.setPolicyName(policyName);
//
//                    GetUserPolicyResult getUserPolicyResult = service.getUserPolicy(getUserPolicyRequest);
//                    String doc = getUserPolicyResult.getPolicyDocument();
//                    System.out.println("there2");
//                }
//
//                ListAttachedGroupPoliciesRequest listAttachedGroupPoliciesRequest = new ListAttachedGroupPoliciesRequest();
//                listAttachedGroupPoliciesRequest.setGroupName(group.getGroupName());
//                ListAttachedGroupPoliciesResult listAttachedGroupPoliciesResult =
//                        service.listAttachedGroupPolicies(listAttachedGroupPoliciesRequest);
//                for (AttachedPolicy policy: listAttachedGroupPoliciesResult.getAttachedPolicies()) {
//                    String doc = getPolicyDoc(policy, service);
//                    String policyName = policy.getPolicyName();
//                    System.out.println("there4");
//                }
//
//            }


//            ListAccessKeysRequest listReq = new ListAccessKeysRequest();
//            listReq.setUserName(user.getUserName());
//            List<AccessKeyMetadata> keyMetadatas = service.listAccessKeys(listReq).getAccessKeyMetadata();
//            List<AccessKeyMetadata> keyMetadatas2 = service.listAccessKeys().getAccessKeyMetadata();
//            System.out.println("there");
//
//        }

//        List<AccessKeyMetadata> resources = service.listAccessKeys().getAccessKeyMetadata();
//        for (AccessKeyMetadata metadata: resources) {
//            GetAccessKeyLastUsedRequest request = new GetAccessKeyLastUsedRequest();
//            request.setAccessKeyId(metadata.getAccessKeyId());
//            GetAccessKeyLastUsedResult result = service.getAccessKeyLastUsed(request);
//            System.out.println("there");
//        }

        AmazonCloudFront service = AwsApiGateway.getAmazonCloudFront(null, null);
        ListDistributionsRequest listDistributionsRequest = new ListDistributionsRequest();
        ListDistributionsResult listDistributionsResult = service.listDistributions(listDistributionsRequest);

//        service.getCloudFrontOriginAccessIdentity()
        for (DistributionSummary distributionSummary: listDistributionsResult.getDistributionList().getItems()) {
            distributionSummary.getOrigins();
            GetDistributionConfigRequest getDistributionConfigRequest = new GetDistributionConfigRequest();
            getDistributionConfigRequest.setId(distributionSummary.getId());

//            GetDistributionConfigResult getDistributionConfigResult = service.getDistributionConfig(getDistributionConfigRequest);
//            DistributionConfig distributionConfig = getDistributionConfigResult.getDistributionConfig();
//            System.out.println(getDistributionConfigResult.getDistributionConfig());

        }
        System.out.println("here");
    }

    private static String getPolicyDoc(AttachedPolicy policy, AmazonIdentityManagement service) {
        String policyName = policy.getPolicyName();
        GetPolicyRequest getPolicyRequest = new GetPolicyRequest();
        getPolicyRequest.setPolicyArn(policy.getPolicyArn());
        GetPolicyResult getPolicyResult = service.getPolicy(getPolicyRequest);
        String defaultVersionId = getPolicyResult.getPolicy().getDefaultVersionId();
        GetPolicyVersionRequest getPolicyVersionRequest = new GetPolicyVersionRequest();
        getPolicyVersionRequest.setPolicyArn(policy.getPolicyArn());
        getPolicyVersionRequest.setVersionId(defaultVersionId);
        GetPolicyVersionResult getPolicyVersionResult = service.getPolicyVersion(getPolicyVersionRequest);
        String doc = getPolicyVersionResult.getPolicyVersion().getDocument();
        return doc;
    }

}
