package frc.robot.commands.targeting;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class CenterOnTarget extends Command {
  private Drivetrain drivetrain;
  private Vision vision;

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
