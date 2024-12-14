package frc.robot.commands.targeting;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;

public class TurnToTarget extends Command {
  private Drivetrain drivetrain;
  private double curAngle;

  /**
   * Turn until the Limelight camera is pointing perpendicularly to the target.
   * 
   * @param drt drivetrain subsystem
   */
  public TurnToTarget(Drivetrain drt) {
    drivetrain = drt;
    addRequirements(drivetrain);
  }

  @Override
  public void execute() {
    curAngle = drivetrain.getPose().getRotation().getDegrees();
    drivetrain.drive(0, 0, (-curAngle) / 100); // PID for this?
  }

  @Override
  public void end(boolean interrupted) {
    drivetrain.drive(0, 0, 0);
  }

  @Override
  public boolean isFinished() {
    return Math.abs(curAngle) < 2; // TODO test tolerance
  }
}
