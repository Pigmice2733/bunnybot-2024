package frc.robot.commands.targeting;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class GetToTarget extends Command {
  private Drivetrain drivetrain;
  private Vision vision;

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
