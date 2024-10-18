// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

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

  public static final ShuffleboardTab DRIVETRAIN_TAB = Shuffleboard.getTab("Drivetrain");
  public static final ShuffleboardTab SYSTEMS_TAB = Shuffleboard.getTab("Subsystems");

  public static class CANConfig {
    public static final int INTAKE_PORT = 0;
    public static final int INDEXER_MOTOR_PORT = 0;
    public static final int INDEXER_PISTON_FORWARD_PORT = 0;
    public static final int INDEXER_PISTON_REVERSE_PORT = 0;
    public static final int COLOR_SENSOR_PORT = 0;
    public static final int GRABBER_MOTOR_PORT = 1;
    public static final int GRABBER_FORWARD_PORT = 2;
    public static final int GRABBER_BACKWARD_PORT = 3;
  }
}