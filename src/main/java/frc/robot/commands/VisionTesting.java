package frc.robot.commands;

import com.pathplanner.lib.util.PIDConstants;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.DrivetrainConfig;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class VisionTesting extends Command {
  private Drivetrain dvt;
  private Vision vis;
  private double xOffset, yOffset, rOffset;

  private PIDController xPID, yPID, rPID;
  private PIDConstants drivePID, turnPID;

  private Pose2d target, robotPose;

  public VisionTesting(Drivetrain drivetrain, Vision vision, double xOffset, double yOffset) {
    dvt = drivetrain;
    vis = vision;

    this.xOffset = xOffset;
    this.yOffset = yOffset;
    rOffset = 0;

    addRequirements(dvt, vis);
  }

  @Override
  public void initialize() {
    drivePID = DrivetrainConfig.DRIVE_PID;
    turnPID = dvt.turnPID;

    xPID = new PIDController(drivePID.kP, drivePID.kI, drivePID.kD);
    xPID.setTolerance(0.05);
    yPID = new PIDController(drivePID.kP, drivePID.kI, drivePID.kD);
    yPID.setTolerance(0.05);
    rPID = new PIDController(turnPID.kP, turnPID.kI, turnPID.kD);
    rPID.setTolerance(1);

    getTargetSetpoint();
  }

  @Override
  public void execute() {
    robotPose = dvt.getPose();
    // System.out.println(
    // "Position: " + Constants.round(robotPose.getY(), 2) + ", calculated value: "
    // + Constants.round(calc, 2));

    if (vis.hasTarget()) getTargetSetpoint();

    dvt.drive(
        xPID.calculate(robotPose.getX()),
        yPID.calculate(robotPose.getY()),
        -1 * rPID.calculate(robotPose.getRotation().getDegrees()));
  }

  @Override
  public void end(boolean interrupted) {
    dvt.drive(0, 0, 0);
    System.out.println("Done!");
  }

  @Override
  public boolean isFinished() {
    return xPID.atSetpoint() && yPID.atSetpoint() && rPID.atSetpoint();
  }

  private void getTargetSetpoint() {
    // DOESN'T WORK BUT I DON'T KNOW WHY
   
    dvt.resetPose(new Pose2d());
    target = vis.getTarget();
    
    /* The PID controllers use the robot's pose, not the target pose. */
    xPID.setSetpoint(target.getX() + xOffset);
    yPID.setSetpoint(target.getY() + yOffset);
    rPID.setSetpoint(target.getRotation().getDegrees() + rOffset);

    System.out.println("X: " + xPID.getSetpoint() + ", Y: " + yPID.getSetpoint() + ", turn: " + rPID.getSetpoint());
  }
}
