package edw;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.config.AmazonConfig;
import com.amazonaws.services.config.AmazonConfigClientBuilder;
import com.amazonaws.services.config.model.DescribeDeliveryChannelStatusResult;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import edw.aws.AwsApiGateway;

public class Tmp {
    public static void main(String[] args) {

        AmazonDynamoDB ddb = AwsApiGateway.getAmazonDynamoDB(null);

        ListTablesResult res = ddb.listTables();

        AmazonConfig amazonConfig = AwsApiGateway.getAmazonConfig(null);

        DescribeDeliveryChannelStatusResult result = amazonConfig.describeDeliveryChannelStatus();
        System.out.println("here");
    }
}
