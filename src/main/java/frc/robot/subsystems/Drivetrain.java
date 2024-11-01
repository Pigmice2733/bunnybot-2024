package frc.robot.subsystems;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.DrivetrainConfig;
import swervelib.SwerveDrive;

public class Drivetrain extends SubsystemBase {
  private final SwerveDrive swerve;
  private GenericEntry robotX, robotY, rotation;

  public Drivetrain() {
    swerve = new SwerveDrive(DrivetrainConfig.SWERVE_CONFIG, DrivetrainConfig.SWERVE_CONTROLLER_CONFIG,
        DrivetrainConfig.MAX_DRIVE_SPEED);

    robotX = Constants.DRIVETRAIN_TAB.add("Robot X", 0).withPosition(0, 0).getEntry();
    robotY = Constants.DRIVETRAIN_TAB.add("Robot Y", 0).withPosition(1, 0).getEntry();
    rotation = Constants.DRIVETRAIN_TAB.add("Robot Angle", 0).withPosition(0, 2).getEntry();
  }

  @Override
  public void periodic() {
    robotX.setDouble(swerve.getPose().getX());
    robotY.setDouble(swerve.getPose().getY());
    rotation.setValue(swerve.getPose().getRotation());
  }
}
