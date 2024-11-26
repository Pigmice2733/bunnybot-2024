package frc.robot.commands.targeting;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class TurnToTarget extends Command {
  private Drivetrain drivetrain;
  private Vision vision;

  /**
   * Turn until the Limelight camera is pointing perpendicularly to the target.
   * 
   * @param drt Drivetrain subsystem
   * @param vsn Vision subsystem
   */
  public TurnToTarget(Drivetrain drt, Vision vsn) {
    drivetrain = drt;
    vision = vsn;
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
