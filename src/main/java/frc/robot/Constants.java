// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.util.Color;
import swervelib.encoders.CANCoderSwerve;
import swervelib.imu.NavXSwerve;
import swervelib.motors.SparkMaxSwerve;
import swervelib.parser.PIDFConfig;
import swervelib.parser.SwerveControllerConfiguration;
import swervelib.parser.SwerveDriveConfiguration;
import swervelib.parser.SwerveModuleConfiguration;
import swervelib.parser.SwerveModulePhysicalCharacteristics;
import swervelib.parser.json.MotorConfigDouble;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static final double AXIS_THRESHOLD = 0.1;

  public static final ShuffleboardTab DRIVETRAIN_TAB = Shuffleboard.getTab("Drivetrain & Vision");
  public static final ShuffleboardTab SUBSYSTEM_TAB = Shuffleboard.getTab("Subsystems");

  public static class CANConfig {
    public static final int INDEXER_MOTOR_PORT = 5;
    public static final int INDEXER_FORWARD_PORT = 0;
    public static final int INDEXER_REVERSE_PORT = 0;

    public static final int GRABBER_MOTOR_PORT = 8;
    public static final int GRABBER_FORWARD_PORT = 0;
    public static final int GRABBER_REVERSE_PORT = 0;

    public static final int INTAKE_MOTOR_PORT = 3;
    public static final int INTAKE_LEFT_FORWARD_PORT = 0;
    public static final int INTAKE_LEFT_REVERSE_PORT = 0;
    public static final int INTAKE_RIGHT_FORWARD_PORT = 0;
    public static final int INTAKE_RIGHT_REVERSE_PORT = 0;

    public static final int FRONT_LEFT_DRIVE_PORT = 9;
    public static final int FRONT_LEFT_ANGLE_PORT = 31;
    public static final int FRONT_RIGHT_DRIVE_PORT = 30;
    public static final int FRONT_RIGHT_ANGLE_PORT = 17;
    public static final int BACK_LEFT_DRIVE_PORT = 34;
    public static final int BACK_LEFT_ANGLE_PORT = 33;
    public static final int BACK_RIGHT_DRIVE_PORT = 41;
    public static final int BACK_RIGHT_ANGLE_PORT = 4;
    public static final int FRONT_LEFT_ENCODER_PORT = 0;
    public static final int FRONT_RIGHT_ENCODER_PORT = 0;
    public static final int BACK_LEFT_ENCODER_PORT = 0;
    public static final int BACK_RIGHT_ENCODER_PORT = 0;
  }

  public static class IntakeConfig {
    public static final double INTAKE_MOTOR_SPEED = 0.5;
    public static final double INDEXER_MOTOR_SPEED = 1;
  }

  public static class IndexerConfig {
    public static final Color BALLOON_RED_COLOR = new Color(255, 0, 0);
    public static final Color BALLOON_BLUE_COLOR = new Color(0, 0, 255);
    public static final Color NULL_COLOR = new Color(0, 0, 0);
  }

  public static class GrabberConfig {
    public static final double GRABBER_MOTOR_SPEED = 0.5;
  }

  public static class VisionConfig {
    public static final String CAM_NAME = "";

    public static class Target {
      // degrees away from face-on horizontally, turning counterclockwise increases
      // number
      public double tx;

      // degrees away from face-on vertically, target relatively higher increases
      // number
      public double ty;

      public Target(double x, double y) {
        tx = x;
        ty = y;
      }
    }
  }

  public static class DrivetrainConfig {
    public static final double ROBOT_X_METERS = Units.inchesToMeters(30);
    public static final double ROBOT_Y_METERS = Units.inchesToMeters(30);
    public static final PIDFConfig SWERVE_DRIVE_PID = new PIDFConfig(0.01, 0, 0);
    public static final PIDFConfig SWERVE_ANGLE_PID = new PIDFConfig(0.01, 0, 0);
    public static final MotorConfigDouble SWERVE_CONVERSION_FACTORS = new MotorConfigDouble(16.8, 0.0472867872);
    // TODO: check wheel mu
    public static final SwerveModulePhysicalCharacteristics SWERVE_CHARACTERISTICS = new SwerveModulePhysicalCharacteristics(
        SWERVE_CONVERSION_FACTORS, 1.19, 12, 25, 20, 0.25, 0.25);
    public static final SimpleMotorFeedforward SWERVE_FEEDFORWARD = new SimpleMotorFeedforward(0, 0.2, 0);
    public static final NavXSwerve SWERVE_GYRO = new NavXSwerve(Port.kMXP);

    // TODO: test settings once robot is built
    public static final SwerveModuleConfiguration FRONT_LEFT_MODULE = new SwerveModuleConfiguration(
        new SparkMaxSwerve(CANConfig.FRONT_LEFT_DRIVE_PORT, true),
        new SparkMaxSwerve(CANConfig.FRONT_LEFT_ANGLE_PORT, false), SWERVE_CONVERSION_FACTORS,
        new CANCoderSwerve(CANConfig.FRONT_LEFT_ENCODER_PORT),
        143.52, ROBOT_X_METERS / 2, ROBOT_Y_METERS / 2, SWERVE_ANGLE_PID, SWERVE_DRIVE_PID, SWERVE_CHARACTERISTICS,
        false, true, true,
        "front left", false);

    public static final SwerveModuleConfiguration FRONT_RIGHT_MODULE = new SwerveModuleConfiguration(
        new SparkMaxSwerve(CANConfig.FRONT_RIGHT_DRIVE_PORT, true),
        new SparkMaxSwerve(CANConfig.FRONT_RIGHT_ANGLE_PORT, false), SWERVE_CONVERSION_FACTORS,
        new CANCoderSwerve(CANConfig.FRONT_RIGHT_ENCODER_PORT),
        107.7, -ROBOT_X_METERS / 2, ROBOT_Y_METERS / 2, SWERVE_ANGLE_PID, SWERVE_DRIVE_PID, SWERVE_CHARACTERISTICS,
        false, true, true,
        "front right", false);

    public static final SwerveModuleConfiguration BACK_LEFT_MODULE = new SwerveModuleConfiguration(
        new SparkMaxSwerve(CANConfig.BACK_LEFT_DRIVE_PORT, true),
        new SparkMaxSwerve(CANConfig.BACK_LEFT_ANGLE_PORT, false), SWERVE_CONVERSION_FACTORS,
        new CANCoderSwerve(CANConfig.BACK_LEFT_ENCODER_PORT),
        42.4, ROBOT_X_METERS / 2, -ROBOT_Y_METERS / 2, SWERVE_ANGLE_PID, SWERVE_DRIVE_PID, SWERVE_CHARACTERISTICS,
        false, true, true,
        "back left", false);

    public static final SwerveModuleConfiguration BACK_RIGHT_MODULE = new SwerveModuleConfiguration(
        new SparkMaxSwerve(CANConfig.BACK_RIGHT_DRIVE_PORT, true),
        new SparkMaxSwerve(CANConfig.BACK_RIGHT_ANGLE_PORT, false), SWERVE_CONVERSION_FACTORS,
        new CANCoderSwerve(CANConfig.BACK_LEFT_ENCODER_PORT),
        165.93, -ROBOT_X_METERS / 2, -ROBOT_Y_METERS / 2, SWERVE_ANGLE_PID, SWERVE_DRIVE_PID, SWERVE_CHARACTERISTICS,
        false, true, true,
        "front left", false);

    public static final SwerveModuleConfiguration[] MODULE_ARRAY = { FRONT_LEFT_MODULE, FRONT_RIGHT_MODULE,
        BACK_LEFT_MODULE, BACK_RIGHT_MODULE };
    public static final SwerveDriveConfiguration SWERVE_CONFIG = new SwerveDriveConfiguration(MODULE_ARRAY,
        SWERVE_GYRO, false, SWERVE_FEEDFORWARD, SWERVE_CHARACTERISTICS);

    public static final PIDFConfig SWERVE_HEADING_PID = new PIDFConfig(0.4, 0, 0.01);
    public static final double MAX_DRIVE_SPEED = 4.0;
    public static final double MAX_TURN_SPEED = 180;
    public static final SwerveControllerConfiguration SWERVE_CONTROLLER_CONFIG = new SwerveControllerConfiguration(
        SWERVE_CONFIG, SWERVE_HEADING_PID, 0.3, MAX_DRIVE_SPEED);

    public static final double SLOWMODE_MULTIPLIER = 0.3;

    public static final PIDController DRIVETRAIN_PID_CONTROLLER = new PIDController(0.3, 0, 0);

  }

  public static class AutoConfig {
    public static enum AutoRoutine {
      LEFT_CLOSE, LEFT_MID, LEFT_FAR, RIGHT_CLOSE, RIGHT_MID, RIGHT_FAR, LEFT_LOW, RIGHT_LOW
    }

    public static final ReplanningConfig REPLANNING_CONFIG = new ReplanningConfig(true, false);
    public static final HolonomicPathFollowerConfig PATH_FOLLOWER_CONFIG = new HolonomicPathFollowerConfig(
        DrivetrainConfig.MAX_DRIVE_SPEED,
        Math.sqrt(Math.pow(DrivetrainConfig.ROBOT_X_METERS, 2) + Math.pow(DrivetrainConfig.ROBOT_Y_METERS, 2)),
        REPLANNING_CONFIG);
  }
}