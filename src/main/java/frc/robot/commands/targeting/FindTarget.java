package frc.robot.commands.targeting;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class FindTarget extends Command {
  private Drivetrain drivetrain;
  private Vision vision;
  private double curAngle;
  private boolean clockwise;

  /**
   * Turn until the Limelight camera sees a target.
   * 
   * @param drt drivetrain subsystem
   * @param vsn vision subsystem
   */
  public FindTarget(Drivetrain drt, Vision vsn) {
    drivetrain = drt;
    vision = vsn;
    addRequirements(drivetrain, vision);
  }

  @Override
  public void initialize() {
    clockwise = true;
  }

  @Override
  public void execute() {
    curAngle = drivetrain.getPose().getRotation().getDegrees();
    if (clockwise) {
      drivetrain.drive(0, 0, -0.3);
      if (curAngle >= 45) {
        clockwise = false;
      }
    } else {
      drivetrain.drive(0, 0, 0.3);
    }
  }

  @Override
  public void end(boolean interrupted) {
    drivetrain.drive(0, 0, 0);
  }

  @Override
  public boolean isFinished() {
    return (vision.hasTarget()) || (curAngle <= -45);
    // rest of DriveToTarget is useless if this ends the second way, any way to stop
    // it?
  }
}
