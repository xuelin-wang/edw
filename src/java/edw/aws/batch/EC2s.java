package edw.aws.batch;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.AttachVolumeRequest;
import com.amazonaws.services.ec2.model.AttachVolumeResult;
import com.amazonaws.services.ec2.model.CreateSnapshotRequest;
import com.amazonaws.services.ec2.model.CreateSnapshotResult;
import com.amazonaws.services.ec2.model.CreateVolumeRequest;
import com.amazonaws.services.ec2.model.CreateVolumeResult;
import com.amazonaws.services.ec2.model.CreateVpcRequest;
import com.amazonaws.services.ec2.model.CreateVpcResult;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusRequest;
import com.amazonaws.services.ec2.model.DescribeVolumesRequest;
import com.amazonaws.services.ec2.model.DetachVolumeRequest;
import com.amazonaws.services.ec2.model.Placement;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.TagSpecification;
import com.amazonaws.services.ec2.waiters.AmazonEC2Waiters;
import com.amazonaws.waiters.Waiter;
import com.amazonaws.waiters.WaiterParameters;
import com.google.common.base.Preconditions;

import java.util.Collections;
import java.util.logging.Logger;
import static edw.util.EDLog.SEP;

public class EC2s {
    private static final Logger logger = Logger.getLogger(EC2s.class.getName());

    /**
     *
     * @param ec2
     * @param sleepSeconds
     * @param snapshotsCount
     * @param volumeId example in dev-insecure: vol-073aa3de05f22887e, vol-03dc99306cce1ca45
     * @param namePrefix
     * @throws InterruptedException
     */
    public static void createSnapshots(AmazonEC2 ec2, int sleepSeconds, int snapshotsCount, String volumeId, String namePrefix)
            throws InterruptedException {
        for (int ii = 0; ii < snapshotsCount; ii++) {
            logger.info("Creating snapshot" + SEP + " snapshot_index=" + ii);
            CreateSnapshotRequest createSnapshotRequest = new CreateSnapshotRequest();
            createSnapshotRequest.setDescription("snapshot_" + namePrefix + "_" + ii);
            createSnapshotRequest.setVolumeId(volumeId);
            CreateSnapshotResult createSnapshotResult = ec2.createSnapshot(createSnapshotRequest);
            logger.info("Created snapshot" + SEP + " snapshot_id=" + createSnapshotResult.getSnapshot().getSnapshotId());

            logger.info("Sleeping " + sleepSeconds + " seconds...");
            Thread.sleep(sleepSeconds * 1000);
        }
        logger.info("Done creating snapshots");
    }

    public static void deleteInstances(AmazonEC2 ec2, int instancesCount, String imageId, String instanceType, String keyName,
                                       int minCount, int maxCount, String availabilityZone,
                                       char deviceStartLetter, int sleepSeconds)
    {
        for (int ii = 0; ii < instancesCount; ii++) {

        }

            DetachVolumeRequest detachVolumeRequest = new DetachVolumeRequest();
        char letter = 'a';
        detachVolumeRequest.setDevice("/dev/sd" + letter);
    }

    /**
     * '
     * @param imageId example "ami-bf4193c7"
     * @param instanceType example “t1.micro”, “t2.nano” etc
     * @param keyName example "xwangDev"
     * @param minCount example: 1
     * @param maxCount example: 1
     * @param availabilityZone
     * @param sleepSeconds example: 60
     */
    public static void createInstances(AmazonEC2 ec2, int instancesCount, String imageId, String instanceType, String keyName,
                                       int minCount, int maxCount, String availabilityZone,
                                       String instancetagKey, String instanceTagValue,
                                       int sleepSeconds) throws InterruptedException {
        Preconditions.checkNotNull(instancetagKey);

        RunInstancesRequest runInstancesRequest = new RunInstancesRequest();
        runInstancesRequest.setImageId(imageId);
        runInstancesRequest.setInstanceType(instanceType);
        runInstancesRequest.setMinCount(minCount);
        runInstancesRequest.setMaxCount(maxCount);
        runInstancesRequest.setKeyName(keyName);

        TagSpecification tagSpecification = new TagSpecification();
        tagSpecification.setResourceType("instance");
        tagSpecification.setTags(Collections.singletonList(new Tag(instancetagKey, instanceTagValue)));
        runInstancesRequest.setTagSpecifications(Collections.singletonList(tagSpecification));

        Placement placement = new Placement();
        placement.setAvailabilityZone(availabilityZone);
        runInstancesRequest.setPlacement(placement);

        AmazonEC2Waiters waiters = new AmazonEC2Waiters(ec2);

        for (int ii = 0; ii < instancesCount; ii++) {
            logger.info("Creating instance" + SEP + " instance_index=" + ii);
            RunInstancesResult runInstancesResult = ec2.runInstances(runInstancesRequest);
            String instanceId = runInstancesResult.getReservation().getInstances().get(0).getInstanceId();
            logger.info("Created instance" + SEP + " instance_id=" + instanceId);

            DescribeInstanceStatusRequest describeInstanceStatusRequest = new DescribeInstanceStatusRequest();
            describeInstanceStatusRequest.setInstanceIds(Collections.singletonList(instanceId));
            Waiter<DescribeInstanceStatusRequest> Instwaiter = waiters.instanceStatusOk();
            Instwaiter.run(new WaiterParameters<>(describeInstanceStatusRequest));
            logger.info("Instance is status ok" + SEP + " instance_id=" + instanceId);

            logger.info("Sleeping... " + SEP + "sleep_secs=" + sleepSeconds);
            Thread.sleep(sleepSeconds * 1000);

//            createVolumesForInstances(2, availabilityZone, deviceStartLetter, instanceId, ec2);

            logger.info("All done" + SEP + "instance_id=" + instanceId);
        }
    }

    public static void findInstancesByTag(String instanceTagKey, String instanceTagValue, AmazonEC2 ec2) {

    }

    public static void createVPC(AmazonEC2 ec2) {
        CreateVpcRequest createVpcRequest = new CreateVpcRequest();

        CreateVpcResult createVpcResult = ec2.createVpc(createVpcRequest);
    }

    public static void createVolumesForInstances(int volumesCount, String availabilityZone, Character deviceStartLetter,
                                                 String instanceTagKey, String instanceTagValue, AmazonEC2 ec2) {
        AmazonEC2Waiters waiters = new AmazonEC2Waiters(ec2);
        CreateVolumeRequest createVolumeRequest = new CreateVolumeRequest();
        createVolumeRequest.setAvailabilityZone(availabilityZone);
        createVolumeRequest.setSize(1);
        createVolumeRequest.setVolumeType("gp2");

        for (int jj = 0; jj < volumesCount; jj++) {
            CreateVolumeResult createVolumeResult = ec2.createVolume(createVolumeRequest);
            String volumeId = createVolumeResult.getVolume().getVolumeId();
            logger.info("Created volume" + SEP + " volume_id=" + volumeId);

            Waiter<DescribeVolumesRequest> volumeWaiter = waiters.volumeAvailable();
            DescribeVolumesRequest describeVolumesRequest = new DescribeVolumesRequest();
            describeVolumesRequest.setVolumeIds(Collections.singletonList(volumeId));
            volumeWaiter.run(new WaiterParameters<>(describeVolumesRequest));
            logger.info("Volume is available" + SEP + " volume_id=" + volumeId);

            AttachVolumeRequest attachVolumeRequest = new AttachVolumeRequest();
            char letter = (char)(deviceStartLetter + jj);
            attachVolumeRequest.setDevice("/dev/sd" + letter);
//            attachVolumeRequest.setInstanceId(instanceId);
            attachVolumeRequest.setVolumeId(volumeId);
            AttachVolumeResult attachVolumeResult = ec2.attachVolume(attachVolumeRequest);
            logger.info("Attached instance volume" + SEP + " instance_id=" +
                    attachVolumeResult.getAttachment().getInstanceId() +
                    SEP + "volume_id=" + volumeId);
        }

    }
}
