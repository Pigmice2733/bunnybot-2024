package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.Constants.VisionConfig;

public class Vision extends SubsystemBase {
  private boolean hasTarget;

  private ShuffleboardLayout visionEntries;
  private GenericEntry targetX, targetY, targetAngle;

  private double[] target;
  private Pose2d targetPose;

  /** Finds and uses AprilTags and other vision targets. */
  public Vision() {
    target = new double[6];
    targetPose = new Pose2d();

    hasTarget = false;

    visionEntries = Constants.DRIVETRAIN_TAB.getLayout("Vision", BuiltInLayouts.kList).withSize(2, 4)
        .withPosition(3, 0);
    targetX = visionEntries.add("Target X Offset", 0).withPosition(0, 0).getEntry();
    targetY = visionEntries.add("Target Y Offset", 0).withPosition(0, 1).getEntry();
    targetAngle = visionEntries.add("Target Angle Offset", 0).withPosition(0, 2).getEntry();
  }

  @Override
  public void periodic() {
    target = NetworkTableInstance.getDefault().getTable("limelight").getEntry("targetpose_cameraspace")
        .getDoubleArray(target);

    // Position of the target with relation to the center of the robot.
    targetPose = new Pose2d(target[2] * -1.0 + target[4] * 0.025,
        target[0] + target[4] * 0.04,
        new Rotation2d(Units.degreesToRadians(target[4]) * -1)).plus(VisionConfig.CAMERA_OFFSET);

    hasTarget = LimelightHelpers.getTV("");

    updateEntries();
    // updatePrints();
  }

  private void updateEntries() {
    targetX.setDouble(hasTarget ? Constants.round(targetPose.getX(), 1) : 0);
    targetY.setDouble(hasTarget ? Constants.round(targetPose.getY(), 1) : 0);
    targetAngle.setDouble(hasTarget ? Constants.round(targetPose.getRotation().getDegrees(), 1) : 0);
  }

  private void updatePrints() {
    String output = "";

    // output += (LimelightHelpers.getLimelightNTTable("").equals(NetworkTableInstance.getDefault().getTable("limelight")) && !LimelightHelpers.getLimelightNTTable("output").equals(null)) ? "Table passed. " : "Table failed. ";
    // output += (LimelightHelpers.getLimelightNTTableEntry("", "t2d").equals(NetworkTableInstance.getDefault().getTable("limelight").getEntry("t2d")) && !LimelightHelpers.getLimelightNTTableEntry("", "t2d").equals(null)) ? "Entry passed. " : "Entry failed. ";
    // output += LimelightHelpers.getT2DArray("").equals(NetworkTableInstance.getDefault().getTable("limelight").getEntry("t2d").getDoubleArray(new double[6])) ? "Array equal. " : "Array not equal. ";
    // output += LimelightHelpers.getT2DArray("").equals(null) ? "Array null." : "Array not null.";
    // output += (LimelightHelpers.getTargetCount("") == (int) NetworkTableInstance.getDefault().getTable("limelight").getEntry("t2d").getDoubleArray(new double[6])[1]) ? "Count passed." : "Count failed.";
    
    for (double i : LimelightHelpers.getT2DArray("")) {output += String.valueOf(i);}
    for (double i : LimelightHelpers.getLimelightNTTableEntry("", "t2d").getDoubleArray(new double[6])) {output += String.valueOf(i);}

    System.out.println(output);
  }

  /** Returns true when there is a visible target. */
  public boolean hasTarget() {
    return hasTarget;
  }

  public Pose2d getTarget() {
    if (hasTarget)
      return targetPose;
    return null;
  }
}
