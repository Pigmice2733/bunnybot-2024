package frc.robot.commands.targeting;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.DrivetrainConfig;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class GetToTarget extends Command {
  private Drivetrain drivetrain;
  private Vision vision;
  private double startAngle, ty;

  /**
   * Move forward until the target is out of view, which is a couple of feet in
   * front of the target.
   * 
   * @param drt Drivetrain subsystem
   * @param vsn Vision subsystem
   */
  public GetToTarget(Drivetrain drt, Vision vsn) {
    drivetrain = drt;
    vision = vsn;
  }

  @Override
  public void initialize() {
    startAngle = drivetrain.robotPose.getRotation().getDegrees();
  }

  @Override
  public void execute() {
    ty = Math.abs(vision.getTarget().ty);
    if (startAngle < 10 && startAngle > -10) {
      drivetrain.drive((30 - ty) * 0.04 * DrivetrainConfig.MAX_DRIVE_SPEED, 0, 0);
    } else if (startAngle < 100 && startAngle > 80) {
      drivetrain.drive(0, (30 - ty) * -0.04 * DrivetrainConfig.MAX_DRIVE_SPEED, 0);
    } else if (startAngle < -80 && startAngle > -100) {
      drivetrain.drive(0, (30 - ty) * 0.04 * DrivetrainConfig.MAX_DRIVE_SPEED, 0);
    } else if (startAngle < -170 || startAngle > 170) {
      drivetrain.drive((30 - ty) * -0.04 * DrivetrainConfig.MAX_DRIVE_SPEED, 0, 0);
    } else {
      end(true);
    }
  }

  @Override
  public void end(boolean interrupted) {
    drivetrain.drive(0, 0, 0);
  }

  @Override
  public boolean isFinished() {
    return !vision.hasTarget();
  }
}
