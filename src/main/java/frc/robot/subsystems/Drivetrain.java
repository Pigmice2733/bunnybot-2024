package frc.robot.subsystems;

import java.io.File;
import java.io.IOException;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.networktables.GenericEntry;
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
import swervelib.imu.SwerveIMU;
import swervelib.parser.SwerveParser;

public class Drivetrain extends SubsystemBase {
  private /* final */ SwerveDrive swerve;
  private final SwerveIMU gyro;
  private final SwerveDriveKinematics kinematics;
  private final SwerveDriveOdometry odometry;
  private Pose2d robotPose;

  private SwerveModulePosition[] modulePositions = new SwerveModulePosition[4];
  private SwerveModule[] modules = new SwerveModule[4];

  private ShuffleboardLayout drivetrainEntries, swerveEntries;
  private GenericEntry robotX, robotY, rotation, frontLeftEntry, frontRightEntry, backLeftEntry, backRightEntry;

  public Drivetrain(Alliance alliance) {
    try {
      swerve = new SwerveParser(new File(Filesystem.getDeployDirectory(), "swerve"))
          .createSwerveDrive(DrivetrainConfig.MAX_DRIVE_SPEED);
    } catch (IOException e) {
      e.printStackTrace();
    }

    modules = swerve.getModules();
    modulePositions = swerve.getModulePositions();
    kinematics = swerve.kinematics;
    gyro = swerve.getGyro();
    odometry = new SwerveDriveOdometry(kinematics, gyro.getRotation3d().toRotation2d(), modulePositions);
    robotPose = new Pose2d();
    resetPose(robotPose);

    drivetrainEntries = Constants.DRIVETRAIN_TAB.getLayout("Drivetrain", BuiltInLayouts.kList).withSize(1, 3)
        .withPosition(0, 0);
    robotX = drivetrainEntries.add("Robot X", 0).withPosition(0, 0).getEntry();
    robotY = drivetrainEntries.add("Robot Y", 0).withPosition(1, 0).getEntry();
    rotation = drivetrainEntries.add("Robot Angle", 0).withPosition(2, 0).getEntry();

    swerveEntries = Constants.DRIVETRAIN_TAB.getLayout("Swerve Modules", BuiltInLayouts.kList).withSize(1, 4)
        .withPosition(1, 0);
    frontLeftEntry = swerveEntries.add("Front Left Encoder Output", 0).withPosition(0, 0).getEntry();
    frontRightEntry = swerveEntries.add("Front Right Encoder Output", 0).withPosition(1, 0).getEntry();
    backLeftEntry = swerveEntries.add("Back Left Encoder Output", 0).withPosition(2, 0).getEntry();
    backRightEntry = swerveEntries.add("Back Right Encoder Output", 0).withPosition(3, 0).getEntry();

    AutoBuilder.configureHolonomic(
        swerve::getPose,
        (pose) -> swerve.resetOdometry(pose),
        swerve::getRobotVelocity,
        (speed) -> drive(speed.vxMetersPerSecond, speed.vyMetersPerSecond, speed.omegaRadiansPerSecond),
        AutoConfig.PATH_FOLLOWER_CONFIG,
        () -> alliance == Alliance.Red,
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
  private void updateModulePositions() {
    modulePositions[0] = modules[0].getPosition();
    modulePositions[1] = modules[1].getPosition();
    modulePositions[2] = modules[2].getPosition();
    modulePositions[3] = modules[3].getPosition();
  }

  private void updateEntries() {
    robotX.setDouble(robotPose.getX());
    robotY.setDouble(robotPose.getY());
    rotation.setValue(robotPose.getRotation().getDegrees());

    frontLeftEntry.setDouble(modules[0].getAbsolutePosition());
    frontRightEntry.setDouble(modules[1].getAbsolutePosition());
    backLeftEntry.setDouble(modules[2].getAbsolutePosition());
    backRightEntry.setDouble(modules[3].getAbsolutePosition());
  }

  /** Returns the drivetrain as a SwerveDrive object. */
  public SwerveDrive getSwerve() {
    return swerve;
  }

  /** Returns the current pose of the robot. */
  public Pose2d getPose() {
    return new Pose2d(-swerve.getPose().getX(), swerve.getPose().getY(), swerve.getPose().getRotation());
  }

  /** Sets the robot odometry to the given pose. */
  public void resetPose(Pose2d pose) {
    swerve.resetOdometry(pose);
  }

  /**
   * Drives the robot in field-oriented mode by creating a ChassisSpeeds object.
   * Positive X is to the right from the driver's perspective; positive Y is away
   * from the alliance wall.
   */
  public void drive(double driveSpeedX, double driveSpeedY, double turnSpeed) {
    swerve.driveFieldOriented(new ChassisSpeeds(driveSpeedY, driveSpeedX, turnSpeed));
  }

  public Command reset() {
    return new InstantCommand(() -> resetPose(new Pose2d()));
  }
}
