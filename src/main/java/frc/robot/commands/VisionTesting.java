package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.DrivetrainConfig;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class VisionTesting extends Command {
  private Drivetrain dvt;
  private Vision vis;
  private PIDController xPID, yPID, rPID;
  private Pose2d target, robotPose;

  public VisionTesting(Drivetrain drivetrain, Vision vision) {
    dvt = drivetrain;
    vis = vision;

    xPID = yPID = rPID = DrivetrainConfig.DRIVETRAIN_PID_CONTROLLER;
    xPID.setTolerance(0.1);
    yPID.setTolerance(0.1);
    rPID.setTolerance(0.5);

    addRequirements(dvt, vis);
  }

  @Override
  public void initialize() {
    dvt.resetPose(new Pose2d());
    target = vis.getTarget();

    xPID.setSetpoint(target.getX() + 2.0);
    yPID.setSetpoint(target.getY());
    rPID.setSetpoint(target.getRotation().getDegrees() * -1.0);
  }

  @Override
  public void execute() {
    robotPose = dvt.getPose();
    dvt.drive(
        xPID.calculate(robotPose.getX()),
        yPID.calculate(robotPose.getY()),
        rPID.calculate(robotPose.getRotation().getDegrees()));
  }

  @Override
  public void end(boolean interrupted) {
    dvt.drive(0, 0, 0);
  }

  @Override
  public boolean isFinished() {
    return xPID.atSetpoint() && yPID.atSetpoint() && rPID.atSetpoint();
  }
}
