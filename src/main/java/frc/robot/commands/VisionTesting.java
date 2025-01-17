package frc.robot.commands;

import com.pathplanner.lib.util.PIDConstants;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.Constants.DrivetrainConfig;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class VisionTesting extends Command {
  private Drivetrain dvt;
  private Vision vis;

  private PIDController xPID, yPID, rPID;
  private PIDConstants drivePID, turnPID;

  private Pose2d target, robotPose;
  private double calc;

  public VisionTesting(Drivetrain drivetrain, Vision vision) {
    dvt = drivetrain;
    vis = vision;

    drivePID = DrivetrainConfig.DRIVE_PID;
    turnPID = DrivetrainConfig.TURN_PID;

    xPID = new PIDController(drivePID.kP, drivePID.kI, drivePID.kD);
    xPID.setTolerance(0.1);

    yPID = new PIDController(drivePID.kP, drivePID.kI, drivePID.kD);
    yPID.setTolerance(0.1);

    rPID = new PIDController(turnPID.kP, turnPID.kI, turnPID.kD);
    rPID.setTolerance(0.5);

    addRequirements(dvt, vis);
  }

  @Override
  public void initialize() {
    dvt.resetPose(new Pose2d());
    target = vis.getTarget();

    /* The PID controllers use the robot's pose, not the target pose. */
    xPID.setSetpoint(target.getX() + 4.0);
    yPID.setSetpoint(target.getY());
    rPID.setSetpoint(target.getRotation().getDegrees() * -1.0);
    System.out.println(yPID.getSetpoint());
  }

  @Override
  public void execute() {
    robotPose = dvt.getPose();
    calc = yPID.calculate(robotPose.getY());
    System.out.println(
        "Position: " + Constants.round(robotPose.getY(), 1) + ", calculated value: " + Constants.round(calc, 1));
    dvt.drive(
        // -1 * xPID.calculate(robotPose.getX()),
        0, -1 * calc, 0);
    // rPID.calculate(robotPose.getRotation().getDegrees()));
  }

  @Override
  public void end(boolean interrupted) {
    dvt.drive(0, 0, 0);
  }

  @Override
  public boolean isFinished() {
    return /* xPID.atSetpoint() && */ yPID.atSetpoint() /* && rPID.atSetpoint() */;
  }
}
