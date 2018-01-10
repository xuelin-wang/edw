package edw;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupReferencesRequest;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupReferencesResult;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsRequest;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsResult;
import com.amazonaws.services.ec2.model.SecurityGroup;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.model.AttachedPolicy;
import com.amazonaws.services.identitymanagement.model.GetPolicyRequest;
import com.amazonaws.services.identitymanagement.model.GetPolicyResult;
import com.amazonaws.services.identitymanagement.model.GetPolicyVersionRequest;
import com.amazonaws.services.identitymanagement.model.GetPolicyVersionResult;
import com.amazonaws.services.identitymanagement.model.GetUserPolicyRequest;
import com.amazonaws.services.identitymanagement.model.GetUserPolicyResult;
import com.amazonaws.services.identitymanagement.model.User;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Grant;
import com.amazonaws.services.s3.model.Grantee;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.util.json.Jackson;
import edw.aws.api.AwsApiGateway;
import edw.aws.batch.S3s;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Tmp {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InterruptedException {

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

        String region = "ca-central-1";
//        AmazonS3 s3 = AwsApiGateway.getAWSService(null, "devinsecure", null,
//                null, AmazonS3.class);
//
//        S3s.deleteBuckets("xwang.*", "us-east-2", s3);
//        S3s.createAndWaitForBucket("xwang.test-script.authacp.3", "us-east-2",
//                new Grant[]{
//                    new Grant(GroupGrantee.AuthenticatedUsers, Permission.ReadAcp)
//                },
//                "xwang_bucket", "test-script", s3);


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
//
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
//
//
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

//        AmazonCloudFront service = AwsApiGateway.getAmazonCloudFront(null, null);
//        ListDistributionsRequest listDistributionsRequest = new ListDistributionsRequest();
//        ListDistributionsResult listDistributionsResult = service.listDistributions(listDistributionsRequest);
//
//        for (DistributionSummary distributionSummary: listDistributionsResult.getDistributionList().getItems()) {
//            distributionSummary.getOrigins();
//            GetDistributionConfigRequest getDistributionConfigRequest = new GetDistributionConfigRequest();
//            getDistributionConfigRequest.setId(distributionSummary.getId());
//            ListTagsForResourceRequest listTagsForResourceRequest = new ListTagsForResourceRequest();
//            listTagsForResourceRequest.setResource(distributionSummary.getARN());
//            ListTagsForResourceResult listTagsForResourceResult = service.listTagsForResource(listTagsForResourceRequest);
//            System.out.println("there");
//
//        }
//
//        AWSLambda service = AwsApiGateway.getAWSLambda(null, null);
//        ListDistributionsRequest listDistributionsRequest = new ListDistributionsRequest();
//        List<FunctionConfiguration> functionConfigs = service.listFunctions().getFunctions();
//
//        for (FunctionConfiguration item: functionConfigs) {
//
//
//
//            System.out.println("there");
//
//        }
//


//        AmazonRDS service = AwsApiGateway.getAmazonRDS("us-east-2", null);
//        DescribeDBInstancesResult describeDBInstancesResult = service.describeDBInstances();
//        for (DBInstance dbInstance: describeDBInstancesResult.getDBInstances()) {
//            String str = Jackson.toJsonPrettyString(dbInstance);
//            System.out.println(str);
//        }

//        AWSElasticsearch service = AwsApiGateway.getAWSElasticsearch(null, null);
//        ListDomainNamesResult listDomainNamesResult = service.listDomainNames(new ListDomainNamesRequest());
//        for (DomainInfo domainInfo: listDomainNamesResult.getDomainNames()) {
//            DescribeElasticsearchDomainConfigRequest describeElasticsearchDomainConfigRequest = new DescribeElasticsearchDomainConfigRequest();
//            describeElasticsearchDomainConfigRequest.setDomainName(domainInfo.getDomainName());
//            DescribeElasticsearchDomainConfigResult result = service.describeElasticsearchDomainConfig(describeElasticsearchDomainConfigRequest);
//            String str = Jackson.toJsonPrettyString(result);
//            System.out.println(str);
//        }

//        AWSSecurityTokenService sts = AwsApiGateway.getAAWSSecurityTokenServiceh(null, null);
//        AssumeRoleRequest assumeRoleRequest = new AssumeRoleRequest();
//        assumeRoleRequest.setDurationSeconds(3600);
//        assumeRoleRequest.setRoleSessionName("xwangTest");
//        assumeRoleRequest.setExternalId("xuelinwangdevinsecure");
//        assumeRoleRequest.setRoleArn("arn:aws:iam::551164845140:role/xuelinwang");
//
//        AssumeRoleResult assumeRoleResult = sts.assumeRole(assumeRoleRequest);


//        AmazonEC2 amazonEC2 = AwsApiGateway.getAWSService("us-east-2", null, null, null, AmazonEC2.class);
//
//        DescribeSnapshotsRequest describeSnapshotsRequest = new DescribeSnapshotsRequest();
//        describeSnapshotsRequest.setOwnerIds(Collections.singleton("self"));
//        DescribeSnapshotsResult describeSnapshotsResult = amazonEC2.describeSnapshots(describeSnapshotsRequest);
//
//        String str = Jackson.toJsonPrettyString(describeSnapshotsResult);

//        AmazonRDS amazonRDS = AwsApiGateway.getAWSService("ca-central-1", null, "xwangEvidentQA", "arn:aws:iam::949130717509:role/xwangDevInsecureRole", AmazonRDS.class);
//        AmazonRDS amazonRDS = AwsApiGateway.getAWSService("us-east-1", "evidentiodev", null, null, AmazonRDS.class);

//




        AmazonEC2 amazonEC2 = AwsApiGateway.getAWSService("ca-central-1", "evidentqa", null, null, AmazonEC2.class);

        DescribeSecurityGroupsRequest describeSecurityGroupsRequest = new DescribeSecurityGroupsRequest();
        DescribeSecurityGroupsResult describeSecurityGroupsResult =  amazonEC2.describeSecurityGroups();
        List<SecurityGroup> groups = describeSecurityGroupsResult.getSecurityGroups();
        groups = groups.stream().filter(
                (SecurityGroup sg) -> !"default".equalsIgnoreCase(sg.getGroupName())
        ).collect(Collectors.toList());
        for (SecurityGroup securityGroup: groups) {
            String name = securityGroup.getGroupName();
            String id = securityGroup.getGroupId();
            DescribeSecurityGroupReferencesRequest describeSecurityGroupReferencesRequest = new DescribeSecurityGroupReferencesRequest();
            describeSecurityGroupReferencesRequest.setGroupId(Collections.singletonList(id));
            DescribeSecurityGroupReferencesResult describeSecurityGroupReferencesResult = amazonEC2.describeSecurityGroupReferences(describeSecurityGroupReferencesRequest);
            String str = Jackson.toJsonPrettyString(securityGroup);


        }

//        EC2s.createSnapshots(amazonEC2, 60, 2, "vol-01c74045566e662b2", "xwangTest");




//        EC2s.createInstances(amazonEC2, 5, "ami-55ef662f",
//                "t2.nano", "xwangTestKey",
//                1, 1,
//                "us-east-1a", 'k', 60);


//        DescribeDBInstancesResult  dbResult =  amazonRDS.describeDBInstances();
//        for (DBInstance db: dbResult.getDBInstances()) {
//            DBSubnetGroup dbSubnetGroup = db.getDBSubnetGroup();
//            String a = "";
//            List<String> groupIds = new ArrayList<>();
//            for (VpcSecurityGroupMembership secMember: db.getVpcSecurityGroups()) {
//                groupIds.add(secMember.getVpcSecurityGroupId());
//                String a = "";
//            }
//
//            for (DBSecurityGroupMembership dbsec: db.getDBSecurityGroups()) {
//                String a = "";
//
//            }
//
//            DescribeSecurityGroupsRequest dbreq = new DescribeSecurityGroupsRequest();
//
//            dbreq.setGroupIds(groupIds);
//            DescribeSecurityGroupsResult describeSecurityGroupsResult = amazonEC2.describeSecurityGroups(dbreq);
//            for (SecurityGroup secGroup: describeSecurityGroupsResult.getSecurityGroups()) {
//                String b = "";
//            }
//        }
//

//
//        AWSElasticsearch awsElasticsearch = AwsApiGateway.getAWSService(null, null, AwsApiGateway.EXTERNAL_ID,
//                AwsApiGateway.ROLE_ARN, AWSElasticsearch.class);
//
//        ListDomainNamesRequest listDomainNamesRequest = new ListDomainNamesRequest();
//        ListDomainNamesResult listDomainNamesResult =  awsElasticsearch.listDomainNames(listDomainNamesRequest);
//        List<String> domainNames = new ArrayList<>();
//        for (DomainInfo domainInfo: listDomainNamesResult.getDomainNames()) {
//            domainNames.add(domainInfo.getDomainName());
//        }
//
//        DescribeElasticsearchDomainsRequest domainsRequest = new DescribeElasticsearchDomainsRequest();
//        domainsRequest.setDomainNames(domainNames);
//        DescribeElasticsearchDomainsResult domainsResult = awsElasticsearch.describeElasticsearchDomains(domainsRequest);
//
//        for (ElasticsearchDomainStatus domainStatus: domainsResult.getDomainStatusList()) {
//
//            DescribeElasticsearchDomainConfigRequest configReq = new DescribeElasticsearchDomainConfigRequest();
//            configReq.setDomainName(domainStatus.getDomainName());
//            DescribeElasticsearchDomainConfigResult configResult = awsElasticsearch.describeElasticsearchDomainConfig(configReq);
//
//            String str = "";
//        }


//        for (DomainInfo domainInfo: listDomainNamesResult.getDomainNames()) {
//            DescribeElasticsearchDomainConfigRequest configReq = new DescribeElasticsearchDomainConfigRequest();
//            configReq.setDomainName(domainInfo.getDomainName());
//            DescribeElasticsearchDomainConfigResult configResult = awsElasticsearch.describeElasticsearchDomainConfig(configReq);
//
//            DescribeElasticsearchDomainRequest domainReq = new DescribeElasticsearchDomainRequest();
//            domainReq.setDomainName(domainInfo.getDomainName());
//            DescribeElasticsearchDomainResult domainResult = awsElasticsearch.describeElasticsearchDomain(domainReq);
//
//            String str = "";
//        }
//
//        AmazonIdentityManagement amazonIdentityManagement = AwsApiGateway.getAWSService(null, "devinsecure", null,
//                null, AmazonIdentityManagement.class);
//        AmazonIdentityManagement amazonIdentityManagement = AwsApiGateway.getAWSService(null, null, AwsApiGateway.EXTERNAL_ID,
//                AwsApiGateway.ROLE_ARN, AmazonIdentityManagement.class);

//        List<User> users = amazonIdentityManagement.listUsers().getUsers();
//
//        for (User user: users) {
//            GetUserPolicyRequest getUserPolicyRequest = new GetUserPolicyRequest();
//            getUserPolicyRequest.setUserName(user.getUserName());
//            GetUserPolicyResult getUserPolicyResult = amazonIdentityManagement.getUserPolicy(getUserPolicyRequest);
//            String str = Jackson.toJsonPrettyString(getUserPolicyResult);
//        }
//            GetUserRequest getUserRequest = new GetUserRequest();
//            getUserRequest.setUserName(user.getUserName());
//            GetUserResult getUserResult = amazonIdentityManagement.getUser(getUserRequest);
//
//            ListUserPoliciesRequest listUserPoliciesRequest = new ListUserPoliciesRequest();
//            listUserPoliciesRequest.setUserName(user.getUserName());
//            ListUserPoliciesResult listUserPoliciesResult = amazonIdentityManagement.listUserPolicies(listUserPoliciesRequest);
//
//            for (String policyName: listUserPoliciesResult.getPolicyNames()) {
//                GetUserPolicyRequest getUserPolicyRequest = new GetUserPolicyRequest();
//                getUserPolicyRequest.setUserName(user.getUserName());
//                GetUserPolicyResult getUserPolicyResult = amazonIdentityManagement.getUserPolicy(getUserPolicyRequest);
//
//                ListAttachedUserPoliciesRequest listAttachedUserPoliciesRequest = new ListAttachedUserPoliciesRequest();
//                listAttachedUserPoliciesRequest.setUserName(user.getUserName());
//                ListAttachedUserPoliciesResult listAttachedUserPoliciesResult = amazonIdentityManagement.listAttachedUserPolicies(listAttachedUserPoliciesRequest);
//
//                for (AttachedPolicy attachedPolicy: listAttachedUserPoliciesResult.getAttachedPolicies()) {
//                    GetPolicyRequest getPolicyRequest = new GetPolicyRequest();
//                    getPolicyRequest.setPolicyArn(attachedPolicy.getPolicyArn());
//                    GetPolicyResult getPolicyResult = amazonIdentityManagement.getPolicy(getPolicyRequest);
//
//                    System.out.println(Jackson.toJsonPrettyString(getPolicyResult));
//                }
//
//
//            }
//
//
//            System.out.println(Jackson.toJsonPrettyString(getUserResult));
//        }


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
