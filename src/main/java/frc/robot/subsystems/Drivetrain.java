package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.DrivetrainConfig;
import swervelib.SwerveDrive;
import swervelib.SwerveModule;
import swervelib.imu.SwerveIMU;

public class Drivetrain extends SubsystemBase {
  private final SwerveDrive swerve;
  private final SwerveIMU gyro = DrivetrainConfig.SWERVE_CONFIG.imu;
  private final SwerveDriveKinematics kinematics;
  private final SwerveDriveOdometry odometry;

  private SwerveModulePosition[] modulePositions = new SwerveModulePosition[4];
  private SwerveModule[] modules = new SwerveModule[4];

  private GenericEntry robotX, robotY, rotation;

  public Drivetrain() {
    swerve = new SwerveDrive(DrivetrainConfig.SWERVE_CONFIG, DrivetrainConfig.SWERVE_CONTROLLER_CONFIG,
        DrivetrainConfig.MAX_DRIVE_SPEED);
    modules[0] = new SwerveModule(0, DrivetrainConfig.FRONT_LEFT_MODULE, null);
    modules[1] = new SwerveModule(0, DrivetrainConfig.FRONT_RIGHT_MODULE, null);
    modules[2] = new SwerveModule(0, DrivetrainConfig.BACK_LEFT_MODULE, null);
    modules[3] = new SwerveModule(0, DrivetrainConfig.BACK_RIGHT_MODULE, null);

    kinematics = new SwerveDriveKinematics(
        new Translation2d(DrivetrainConfig.ROBOT_X_METERS, DrivetrainConfig.ROBOT_Y_METERS),
        new Translation2d(-1 * DrivetrainConfig.ROBOT_X_METERS, DrivetrainConfig.ROBOT_Y_METERS),
        new Translation2d(DrivetrainConfig.ROBOT_X_METERS, -1 * DrivetrainConfig.ROBOT_Y_METERS),
        new Translation2d(-1 * DrivetrainConfig.ROBOT_X_METERS, -1 * DrivetrainConfig.ROBOT_Y_METERS));
    odometry = new SwerveDriveOdometry(kinematics, gyro.getRotation3d().toRotation2d(), modulePositions);

    robotX = Constants.DRIVETRAIN_TAB.add("Robot X", 0).withPosition(0, 0).getEntry();
    robotY = Constants.DRIVETRAIN_TAB.add("Robot Y", 0).withPosition(1, 0).getEntry();
    rotation = Constants.DRIVETRAIN_TAB.add("Robot Angle", 0).withPosition(0, 2).getEntry();
  }

  @Override
  public void periodic() {
    // update Shuffleboard entries
    robotX.setDouble(swerve.getPose().getX());
    robotY.setDouble(swerve.getPose().getY());
    rotation.setValue(swerve.getPose().getRotation());

    updateModulePositions();
    odometry.update(gyro.getRotation3d().toRotation2d(), modulePositions);

  }

  public void updateModulePositions() {
    modulePositions[0] = modules[0].getPosition();
    modulePositions[1] = modules[1].getPosition();
    modulePositions[2] = modules[2].getPosition();
    modulePositions[3] = modules[3].getPosition();
  }
}
