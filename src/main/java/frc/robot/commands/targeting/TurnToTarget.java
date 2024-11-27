package frc.robot.commands.targeting;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;

public class TurnToTarget extends Command {
  private Drivetrain drivetrain;
  private double targetAngle, curAngle;

  /**
   * Turn until the Limelight camera is pointing perpendicularly to the target.
   * 
   * @param drt Drivetrain subsystem
   * @param vsn Vision subsystem
   */
  public TurnToTarget(Drivetrain drt) {
    drivetrain = drt;
  }

  @Override
  public void initialize() {
    targetAngle = Math.round(drivetrain.robotPose.getRotation().getDegrees() / 90) * 90;
  }

  @Override
  public void execute() {
    curAngle = drivetrain.robotPose.getRotation().getDegrees();
    drivetrain.drive(0, 0, (targetAngle - curAngle) / 100); // PID for this?
  }

  @Override
  public void end(boolean interrupted) {
    drivetrain.drive(0, 0, 0);
  }

  @Override
  public boolean isFinished() {
    return Math.abs(targetAngle - curAngle) < 2; // test deadband
  }
}
