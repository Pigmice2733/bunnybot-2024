package frc.robot.commands.targeting;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.VisionConfig.Target;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class CenterOnTarget extends Command {
  private Drivetrain drivetrain;
  private Vision vision;
  private double startAngle;
  private Target target;

  /**
   * Move laterally until vision's TX is 0, that is, the robot is directly in
   * front of the target.
   * 
   * @param drt Drivetrain subsystem
   * @param vsn Vision subystem
   */
  public CenterOnTarget(Drivetrain drt, Vision vsn) {
    drivetrain = drt;
    vision = vsn;
  }

  @Override
  public void initialize() {
    startAngle = drivetrain.robotPose.getRotation().getDegrees();
  }

  @Override
  public void execute() {
    target = vision.getTarget();
    if (startAngle < 10 && startAngle > -10) {
      drivetrain.drive(0, target.tx / -60, 0);
    } else if (startAngle < 100 && startAngle > 80) {
      drivetrain.drive(target.tx / -60, 0, 0);
    } else if (startAngle < -80 && startAngle > -100) {
      drivetrain.drive(target.tx / 60, 0, 0);
    } else if (startAngle < -170 || startAngle > 170) {
      drivetrain.drive(0, target.tx / 60, 0);
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
    return Math.abs(target.tx) < 1;
  }
}
