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
    targetX = visionEntries.add("Target X Offset", 0).withPosition(0, 0).getEntry();
    targetY = visionEntries.add("Target Y Offset", 0).withPosition(0, 1).getEntry();
    targetAngle = visionEntries.add("Target Angle Offset", 0).withPosition(0, 2).getEntry();
  }

  @Override
  public void periodic() {
    target = NetworkTableInstance.getDefault().getTable("limelight").getEntry("targetpose_cameraspace")
        .getDoubleArray(target);

    targetPose = new Pose2d(Math.round(target[2] * 100.0) / 100.0,
        Math.round(target[0] * -100.0) / 100.0,
        new Rotation2d(Units.degreesToRadians(target[4] * -1.0)));

    updateEntries();
  }

  private void updateEntries() {
    targetX.setDouble(targetPose.getX());
    targetY.setDouble(targetPose.getY());
    targetAngle.setDouble(Math.round(targetPose.getRotation().getDegrees() * 100.0) / 100.0);
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
