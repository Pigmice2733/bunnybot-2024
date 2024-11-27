package frc.robot.commands.targeting;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class FindTarget extends Command {
  private Drivetrain drivetrain;
  private Vision vision;
  private double startAngle, curAngle;
  private boolean clockwise, allPosAngles;

  /**
   * Turn until the Limelight camera sees a target.
   * 
   * @param drt Drivetrain subsystem
   * @param vsn Vision subsystem
   */
  public FindTarget(Drivetrain drt, Vision vsn) {
    drivetrain = drt;
    vision = vsn;
    addRequirements(drivetrain, vision);
  }

  @Override
  public void initialize() {
    startAngle = drivetrain.getPose().getRotation().getDegrees();
    if (startAngle > 90) {
      allPosAngles = true;
    } else if (startAngle < -90) {
      allPosAngles = true;
      startAngle += 360;
    } else {
      allPosAngles = false;
    }
    clockwise = true;
  }

  @Override
  public void execute() {
    curAngle = drivetrain.getPose().getRotation().getDegrees();
    if (allPosAngles && curAngle < 0) {
      curAngle += 360;
    }
    if (clockwise) {
      drivetrain.drive(0, 0, 0.3);
      if (curAngle >= startAngle + 45) {
        clockwise = false;
      }
    } else {
      drivetrain.drive(0, 0, -0.3); // TEST SPEED
    }
  }

  @Override
  public void end(boolean interrupted) {
    drivetrain.drive(0, 0, 0);
  }

  @Override
  public boolean isFinished() {
    return (vision.hasTarget()) || (curAngle <= startAngle - 45);
    // rest of DriveToTarget is useless if this ends the second way, any way to stop
    // it?
  }
}
