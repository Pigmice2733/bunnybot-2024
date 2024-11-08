package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;

public class MoveToPose extends Command {
  private final Drivetrain drivetrain;
  private Pose2d startPose, endPose;

  public MoveToPose(Drivetrain drivetrain, Pose2d pose) {
    this.drivetrain = drivetrain;
    endPose = pose;
  }

  @Override
  public void initialize() {
    startPose = drivetrain.getPose();
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
