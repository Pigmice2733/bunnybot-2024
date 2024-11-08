package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.DrivetrainConfig;
import frc.robot.subsystems.Drivetrain;

public class MoveToPose extends Command {
  private final Drivetrain drivetrain;
  private Pose2d currentPose, endPose;
  private Translation2d direction;
  private double angleDiff;

  private double xSpeed, ySpeed, turnSpeed;
  private double maxDrive = DrivetrainConfig.MAX_DRIVE_SPEED;
  private double maxTurn = DrivetrainConfig.MAX_TURN_SPEED;

  public MoveToPose(Drivetrain drivetrain, Pose2d pose) {
    this.drivetrain = drivetrain;
    endPose = pose;
  }

  @Override
  public void initialize() {

  }

  @Override
  public void execute() {
    currentPose = drivetrain.getPose();
    direction = endPose.getTranslation().minus(currentPose.getTranslation());
    angleDiff = endPose.getRotation().minus(currentPose.getRotation()).getRadians();

    if (direction.getX() > 1.0) {
      xSpeed = maxDrive;
    } else if (direction.getX() < -1.0) {
      xSpeed = -1 * maxDrive;
    } else {
      xSpeed = direction.getX() / 2;
    }

    if (direction.getY() > 1.0) {
      ySpeed = maxDrive;
    } else if (direction.getY() < -1.0) {
      ySpeed = -1 * maxDrive;
    } else {
      ySpeed = direction.getY() / 2;
    }

    if (angleDiff > 10.0) {
      turnSpeed = maxTurn;
    } else if (angleDiff < -10.0) {
      turnSpeed = -1 * maxTurn;
    } else {
      turnSpeed = angleDiff / 2;
    }

    drivetrain.drive(xSpeed, ySpeed, turnSpeed);
  }

  @Override
  public void end(boolean interrupted) {

  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
