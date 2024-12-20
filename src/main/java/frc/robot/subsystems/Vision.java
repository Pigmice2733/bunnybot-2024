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

    visionEntries = Constants.DRIVETRAIN_TAB.getLayout("Vision", BuiltInLayouts.kList).withSize(2, 4)
        .withPosition(3,
            0);
    targetX = visionEntries.add("Target X Offset", 0).getEntry();
    targetY = visionEntries.add("Target Y Offset", 0).getEntry();
    targetAngle = visionEntries.add("Target Angle Offset", 0).getEntry();
  }

  @Override
  public void periodic() {
    target = NetworkTableInstance.getDefault().getTable("limelight").getEntry("targetpose_cameraspace")
        .getDoubleArray(target);

    // accounts for problems in determining position of target
    targetPose = new Pose2d(25.0 * Math.round(target[0] * 100) / 100.0, 2.0 * Math.round(target[1] * 100) / 100.0,
        new Rotation2d(Units.degreesToRadians(20) - target[5]));

    updateEntries();
  }

  private void updateEntries() {
    targetX.setDouble(targetPose.getX());
    targetY.setDouble(targetPose.getY());
    targetAngle.setDouble(targetPose.getRotation().getDegrees());
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
