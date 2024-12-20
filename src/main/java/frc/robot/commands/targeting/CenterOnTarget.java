package frc.robot.commands.targeting;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class CenterOnTarget extends Command {
  private Drivetrain drivetrain;
  private Vision vision;
  private Pose2d target;

  /**
   * Move laterally until vision's TX is 0, that is, the camera is directly in
   * front of the target.
   * 
   * @param drt drivetrain subsystem
   * @param vsn vision subystem
   */
  public CenterOnTarget(Drivetrain drt, Vision vsn) {
    drivetrain = drt;
    vision = vsn;
    addRequirements(drivetrain, vision);
  }

  @Override
  public void execute() {
    target = vision.getTarget();
    drivetrain.drive(0, target.getX() / -60, 0);
  }

  @Override
  public void end(boolean interrupted) {
    drivetrain.drive(0, 0, 0);
  }

  @Override
  public boolean isFinished() {
    return Math.abs(target.getX()) < 1;
  }
}
