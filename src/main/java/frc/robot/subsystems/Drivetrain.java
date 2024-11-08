package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Controls;
import frc.robot.Constants.DrivetrainConfig;
import swervelib.SwerveDrive;
import swervelib.SwerveModule;
import swervelib.imu.NavXSwerve;

public class Drivetrain extends SubsystemBase {
  private final SwerveDrive swerve;
  private final NavXSwerve gyro;
  private final SwerveDriveKinematics kinematics;
  private final SwerveDriveOdometry odometry;
  public Pose2d robotPose;

  private SwerveModulePosition[] modulePositions = new SwerveModulePosition[4];
  private SwerveModule[] modules = new SwerveModule[4];

  private GenericEntry robotX, robotY, rotation;

  public Drivetrain() {
    swerve = new SwerveDrive(DrivetrainConfig.SWERVE_CONFIG, DrivetrainConfig.SWERVE_CONTROLLER_CONFIG,
        DrivetrainConfig.MAX_DRIVE_SPEED);
    modules[0] = new SwerveModule(0, DrivetrainConfig.FRONT_LEFT_MODULE, DrivetrainConfig.SWERVE_FEEDFORWARD);
    modules[1] = new SwerveModule(1, DrivetrainConfig.FRONT_RIGHT_MODULE, DrivetrainConfig.SWERVE_FEEDFORWARD);
    modules[2] = new SwerveModule(2, DrivetrainConfig.BACK_LEFT_MODULE, DrivetrainConfig.SWERVE_FEEDFORWARD);
    modules[3] = new SwerveModule(3, DrivetrainConfig.BACK_RIGHT_MODULE, DrivetrainConfig.SWERVE_FEEDFORWARD);

    kinematics = new SwerveDriveKinematics(
        new Translation2d(DrivetrainConfig.ROBOT_X_METERS, DrivetrainConfig.ROBOT_Y_METERS),
        new Translation2d(-DrivetrainConfig.ROBOT_X_METERS, DrivetrainConfig.ROBOT_Y_METERS),
        new Translation2d(DrivetrainConfig.ROBOT_X_METERS, -DrivetrainConfig.ROBOT_Y_METERS),
        new Translation2d(-DrivetrainConfig.ROBOT_X_METERS, -DrivetrainConfig.ROBOT_Y_METERS));
    gyro = DrivetrainConfig.SWERVE_GYRO;
    odometry = new SwerveDriveOdometry(kinematics, gyro.getRotation3d().toRotation2d(), modulePositions);
    robotPose = new Pose2d();

    robotX = Constants.DRIVETRAIN_TAB.add("Robot X", 0).withPosition(0, 0).getEntry();
    robotY = Constants.DRIVETRAIN_TAB.add("Robot Y", 0).withPosition(1, 0).getEntry();
    rotation = Constants.DRIVETRAIN_TAB.add("Robot Angle", 0).withPosition(2, 0).getEntry();
  }

  @Override
  public void periodic() {
    robotPose = swerve.getPose();

    // update Shuffleboard entries
    robotX.setDouble(robotPose.getX());
    robotY.setDouble(robotPose.getY());
    rotation.setValue(robotPose.getRotation());

    updateModulePositions();
    odometry.update(gyro.getRotation3d().toRotation2d(), modulePositions);
  }

  public void updateModulePositions() {
    modulePositions[0] = modules[0].getPosition();
    modulePositions[1] = modules[1].getPosition();
    modulePositions[2] = modules[2].getPosition();
    modulePositions[3] = modules[3].getPosition();
  }

  public Pose2d getPose() {
    return swerve.getPose();
  }

  public void drive(double driveSpeedX, double driveSpeedY, double turnSpeed) {
    swerve.driveFieldOriented(new ChassisSpeeds(driveSpeedX, driveSpeedY, turnSpeed));
  }
}
