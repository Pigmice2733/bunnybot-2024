package frc.robot.subsystems;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.AutoConfig;
import frc.robot.Constants.DrivetrainConfig;
import swervelib.SwerveDrive;
import swervelib.SwerveModule;
import swervelib.imu.NavXSwerve;
import swervelib.parser.SwerveParser;

public class Drivetrain extends SubsystemBase {
  private /* final */ SwerveDrive swerve;
  private final NavXSwerve gyro;
  private final SwerveDriveKinematics kinematics;
  private final SwerveDriveOdometry odometry;
  private Pose2d robotPose;

  private SwerveModulePosition[] modulePositions = new SwerveModulePosition[4];
  private SwerveModule[] modules = new SwerveModule[4];

  private ShuffleboardLayout drivetrainEntries;
  private GenericEntry robotX, robotY, rotation;

  private Optional<Alliance> alliance;

  public Drivetrain() {
    try {
      swerve = new SwerveParser(new File(Filesystem.getDeployDirectory(), "swerve"))
          .createSwerveDrive(DrivetrainConfig.MAX_DRIVE_SPEED);
    } catch (IOException e) {
      e.printStackTrace();
      swerve = new SwerveDrive(DrivetrainConfig.SWERVE_CONFIG, DrivetrainConfig.SWERVE_CONTROLLER_CONFIG,
          DrivetrainConfig.MAX_DRIVE_SPEED);
    }

    // swerve = new SwerveDrive(DrivetrainConfig.SWERVE_CONFIG,
    // DrivetrainConfig.SWERVE_CONTROLLER_CONFIG,
    // DrivetrainConfig.MAX_DRIVE_SPEED);
    // modules[0] = new SwerveModule(0, DrivetrainConfig.FRONT_LEFT_MODULE,
    // DrivetrainConfig.SWERVE_FEEDFORWARD);
    // modules[1] = new SwerveModule(1, DrivetrainConfig.FRONT_RIGHT_MODULE,
    // DrivetrainConfig.SWERVE_FEEDFORWARD);
    // modules[2] = new SwerveModule(2, DrivetrainConfig.BACK_LEFT_MODULE,
    // DrivetrainConfig.SWERVE_FEEDFORWARD);
    // modules[3] = new SwerveModule(3, DrivetrainConfig.BACK_RIGHT_MODULE,
    // DrivetrainConfig.SWERVE_FEEDFORWARD);

    kinematics = new SwerveDriveKinematics(
        new Translation2d(DrivetrainConfig.ROBOT_X_METERS, DrivetrainConfig.ROBOT_Y_METERS),
        new Translation2d(-DrivetrainConfig.ROBOT_X_METERS, DrivetrainConfig.ROBOT_Y_METERS),
        new Translation2d(DrivetrainConfig.ROBOT_X_METERS, -DrivetrainConfig.ROBOT_Y_METERS),
        new Translation2d(-DrivetrainConfig.ROBOT_X_METERS, -DrivetrainConfig.ROBOT_Y_METERS));
    gyro = DrivetrainConfig.SWERVE_GYRO;
    odometry = new SwerveDriveOdometry(kinematics, gyro.getRotation3d().toRotation2d(), modulePositions);
    robotPose = new Pose2d();
    resetPose(robotPose);

    drivetrainEntries = Constants.DRIVETRAIN_TAB.getLayout("Drivetrain", BuiltInLayouts.kList).withSize(1, 3)
        .withPosition(0,
            0);
    robotX = drivetrainEntries.add("Robot X", 0).withPosition(0, 0).getEntry();
    robotY = drivetrainEntries.add("Robot Y", 0).withPosition(1, 0).getEntry();
    rotation = drivetrainEntries.add("Robot Angle", 0).withPosition(2, 0).getEntry();

    alliance = DriverStation.getAlliance();

    AutoBuilder.configureHolonomic(
        swerve::getPose,
        (pose) -> swerve.resetOdometry(pose),
        swerve::getRobotVelocity,
        (speed) -> drive(speed.vxMetersPerSecond, speed.vyMetersPerSecond, speed.omegaRadiansPerSecond),
        AutoConfig.PATH_FOLLOWER_CONFIG,
        () -> alliance.isPresent() ? alliance.get() == Alliance.Red : false,
        this);
  }

  @Override
  public void periodic() {
    robotPose = getPose();

    updateEntries();

    updateModulePositions();
    odometry.update(gyro.getRotation3d().toRotation2d(), modulePositions);
  }

  /**
   * Updates the stored values for module positions to the current positions of
   * the modules.
   */
  public void updateModulePositions() {
    modulePositions[0] = modules[0].getPosition();
    modulePositions[1] = modules[1].getPosition();
    modulePositions[2] = modules[2].getPosition();
    modulePositions[3] = modules[3].getPosition();
  }

  /** Returns the drivetrain as a SwerveDrive object. */
  public SwerveDrive getSwerve() {
    return swerve;
  }

  /** Returns the current pose of the robot. */
  public Pose2d getPose() {
    return swerve.getPose();
  }

  /** Sets the robot odometry to the given pose. */
  public void resetPose(Pose2d pose) {
    swerve.resetOdometry(pose);
  }

  /**
   * Drives the robot in field-oriented mode by creating a ChassisSpeeds object.
   * Positive X is away from the alliance wall; positive Y is to the left from the
   * drivers' perspective.
   */
  public void drive(double driveSpeedX, double driveSpeedY, double turnSpeed) {
    swerve.driveFieldOriented(new ChassisSpeeds(driveSpeedX, driveSpeedY, turnSpeed));
  }

  private void updateEntries() {
    robotX.setDouble(robotPose.getX());
    robotY.setDouble(robotPose.getY());
    rotation.setValue(robotPose.getRotation());
  }

  public Command reset() {
    return new InstantCommand(() -> resetPose(new Pose2d()), this);
  }
}
