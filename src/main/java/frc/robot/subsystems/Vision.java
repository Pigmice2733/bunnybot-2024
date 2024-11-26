package frc.robot.subsystems;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.Constants.VisionConfig;
import frc.robot.Constants.VisionConfig.Target;

public class Vision extends SubsystemBase {
  private String camName;

  private Target target;
  private boolean hasTarget;

  private ShuffleboardLayout visionEntries;
  private GenericEntry targetX, targetY, targetArea;

  /** Finds and uses AprilTags and other vision targets. */
  public Vision() {
    camName = VisionConfig.CAM_NAME;

    target = new Target(0, 0, 0);

    visionEntries = Constants.DRIVETRAIN_TAB.getLayout("Vision", BuiltInLayouts.kList).withSize(1, 3)
        .withPosition(1,
            0);
    targetX = visionEntries.add("Target X", 0).getEntry();
    targetY = visionEntries.add("Target Y", 0).getEntry();
    targetArea = visionEntries.add("Target Area", 0).getEntry();
  }

  @Override
  public void periodic() {
    hasTarget = LimelightHelpers.getTV(camName);
    if (hasTarget) {
      target.tx = LimelightHelpers.getTX(camName);
      target.ty = LimelightHelpers.getTY(camName);
      target.ta = LimelightHelpers.getTA(camName);
    } else {
      target.tx = target.ty = target.ta = 0;
    }

    updateEntries();
  }

  private void updateEntries() {
    targetX.setDouble(target.tx);
    targetY.setDouble(target.ty);
    targetArea.setDouble(target.ta);
  }

  /** Returns true when there is a current target. */
  public boolean hasTarget() {
    return hasTarget;
  }

  public Target getTarget() {
    if (hasTarget)
      return target;
    return null;
  }
}
