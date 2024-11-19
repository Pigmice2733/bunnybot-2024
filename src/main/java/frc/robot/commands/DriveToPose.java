package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.DrivetrainConfig;
import frc.robot.subsystems.Drivetrain;

public class DriveToPose extends Command {
  private final Drivetrain drivetrain;
  private Pose2d endPose;

  private PIDController xPID, yPID, rPID = DrivetrainConfig.DRIVETRAIN_PID_CONTROLLER;

  public DriveToPose(Drivetrain drivetrain, Pose2d destination) {
    this.drivetrain = drivetrain;
    endPose = destination;
  }

  @Override
  public void initialize() {
    xPID.setSetpoint(endPose.getX());
    yPID.setSetpoint(endPose.getY());
    rPID.setSetpoint(endPose.getRotation().getRadians());
  }

  @Override
  public void execute() {
    Pose2d currentPose = drivetrain.getPose();
    drivetrain.drive(
        xPID.calculate(currentPose.getX()),
        yPID.calculate(currentPose.getY()),
        rPID.calculate(currentPose.getRotation().getRadians()));
  }

  @Override
  public void end(boolean interrupted) {
    drivetrain.drive(0, 0, 0);
  }

  @Override
  public boolean isFinished() {
    return xPID.atSetpoint() && yPID.atSetpoint() && rPID.atSetpoint();
  }
}
