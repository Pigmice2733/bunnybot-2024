// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.util.Color;

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
    public static final int PNEUMATICS_HUB_PORT = 5;

    public static final int INDEXER_MOTOR_PORT = 4;
    public static final int INDEXER_FORWARD_PORT = 15;
    public static final int INDEXER_REVERSE_PORT = 14;

    public static final int GRABBER_MOTOR_PORT = 8;
    public static final int GRABBER_FORWARD_PORT = 2;
    public static final int GRABBER_REVERSE_PORT = 3;
    public static final int LIMIT_SWITCH_PORT = 0;

    public static final int INTAKE_MOTOR_PORT = 3;
    public static final int INTAKE_FORWARD_PORT = 0;
    public static final int INTAKE_REVERSE_PORT = 1;

    public static final int FRONT_LEFT_DRIVE_PORT = 9;
    public static final int FRONT_LEFT_ANGLE_PORT = 31;
    public static final int FRONT_RIGHT_DRIVE_PORT = 30;
    public static final int FRONT_RIGHT_ANGLE_PORT = 17;
    public static final int BACK_LEFT_DRIVE_PORT = 34;
    public static final int BACK_LEFT_ANGLE_PORT = 33;
    public static final int BACK_RIGHT_DRIVE_PORT = 41;
    public static final int BACK_RIGHT_ANGLE_PORT = 40;
    public static final int FRONT_LEFT_ENCODER_PORT = 20;
    public static final int FRONT_RIGHT_ENCODER_PORT = 26;
    public static final int BACK_LEFT_ENCODER_PORT = 22;
    public static final int BACK_RIGHT_ENCODER_PORT = 24;
  }

  public static class IntakeConfig {
    public static final double INTAKE_MOTOR_SPEED = 0.4;
    public static final double INDEXER_MOTOR_SPEED = 0.4;
  }

  public static class IndexerConfig {
    public static final Color BALLOON_RED_COLOR = new Color(400, 500, 300);
    public static final Color BALLOON_BLUE_COLOR = new Color(700, 1700, 1400);
    public static final Color NULL_COLOR = new Color(300, 500, 300);
    public static final int IRBalloonPresentThreshold = 5;
    public static final int MinRedThreshold = 300;
    public static final int MinRedBlueDifferential = 50;
  }

  public static class GrabberConfig {
    public static final double GRABBER_MOTOR_SPEED = 0.5;
    public static final double GEAR_RATIO = 1.0 / 500;
    public static final PIDController GRABBER_PID = new PIDController(0.1, 0, 0);
    public static final double PID_TOLERANCE = 0.1; // degrees
    public static final double MAX_ANGLE_PISTON_IN = 77.0; // degrees
    public static final double MAX_ANGLE_PISTON_OUT = 36.0; // degrees
    public static final double GRABBED_TOTE_ANGLE = 5.0; // degrees
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
    public static final double MAX_DRIVE_SPEED = 1.7;
    public static final double MAX_TURN_SPEED = 1.7;
    public static final double SLOWMODE_MULTIPLIER = 0.3;
    public static final PIDController DRIVETRAIN_PID_CONTROLLER = new PIDController(0.3, 0, 0);
  }

  public static class AutoConfig {
    public static enum AutoRoutine {
      LEFT_CLOSE, LEFT_MID, LEFT_FAR, RIGHT_CLOSE, RIGHT_MID, RIGHT_FAR, LEFT_LOW, RIGHT_LOW
    }

    public static final ReplanningConfig REPLANNING_CONFIG = new ReplanningConfig(true, false);
    public static final HolonomicPathFollowerConfig PATH_FOLLOWER_CONFIG = new HolonomicPathFollowerConfig(
        DrivetrainConfig.MAX_DRIVE_SPEED, Math.hypot(14.38, 11.88), REPLANNING_CONFIG);
  }
}