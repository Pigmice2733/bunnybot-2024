package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DrivetrainConfig;
import swervelib.SwerveDrive;

public class Drivetrain extends SubsystemBase {
  private final SwerveDrive swerve;

  public Drivetrain() {
    swerve = new SwerveDrive(DrivetrainConfig.SWERVE_CONFIG, DrivetrainConfig.SWERVE_CONTROLLER_CONFIG,
        DrivetrainConfig.MAX_DRIVE_SPEED);
  }
}
