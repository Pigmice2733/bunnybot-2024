// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.CANConfig;
import frc.robot.Constants.GrabberConfig;

public class Grabber extends SubsystemBase {
  private final CANSparkMax motor;
  private final DoubleSolenoid piston;
  private final DigitalInput limitSwitch;
  private boolean zeroed = false;

  private double angle;
  private PIDController motorController;

  private ShuffleboardLayout grabberEntries;
  private GenericEntry motorEntry, pistonEntry, switchEntry, angleEntry, zeroedEntry;

  /** Creates a new Grabber. */
  public Grabber() {
    motor = new CANSparkMax(CANConfig.GRABBER_MOTOR_PORT, MotorType.kBrushless);
    motor.restoreFactoryDefaults();
    motor.getEncoder().setPositionConversionFactor(GrabberConfig.GEAR_RATIO);

    piston = new DoubleSolenoid(
        CANConfig.PNEUMATICS_HUB_PORT,
        PneumaticsModuleType.REVPH,
        CANConfig.GRABBER_FORWARD_PORT,
        CANConfig.GRABBER_REVERSE_PORT);
    piston.set(Value.kOff);

    limitSwitch = new DigitalInput(CANConfig.LIMIT_SWITCH_PORT);

    angle = 0;

    motorController = GrabberConfig.GRABBER_PID;
    motorController.setTolerance(GrabberConfig.PID_TOLERANCE);

    grabberEntries = Constants.SUBSYSTEM_TAB
        .getLayout("Grabber", BuiltInLayouts.kList)
        .withSize(1, 2)
        .withPosition(2, 0);
    motorEntry = grabberEntries.add("Motor Speed", 0).withWidget(BuiltInWidgets.kNumberSlider).getEntry();
    angleEntry = grabberEntries.add("Grabber Angle", 0).getEntry();
    pistonEntry = grabberEntries.add("Piston Value", "In").getEntry();
    switchEntry = grabberEntries.add("Limit Switch", false).getEntry();
    zeroedEntry = grabberEntries.add("Zeroed", false).getEntry();
  }

  /*
   * public void setZeroing(boolean zeroing) {
   * this.zeroing = zeroing;
   * }
   */

  @Override
  public void periodic() {
    if (getSwitch() && motor.get() < 0) {
      stopMotor();
    } else {
      angle = motor.getEncoder().getPosition();
    }

    updateEntries();
  }

  public void unzero() {
    zeroed = false;
  }

  public void zero() {
    angle = 0;
    motor.getEncoder().setPosition(0);
    zeroed = true;
    // zeroing = false;
  }

  private void updateEntries() {
    motorEntry.setDouble(motor.get());
    switchEntry.setBoolean(getSwitch());
    angleEntry.setDouble(angle);
    zeroedEntry.setBoolean(zeroed);
  }

  // returns true of okay to set speed to the requested speed
  public boolean applySoftwareStops(double speed) {
    if (!zeroed && speed > 0) {
      // if we aren't zeroed we only rotate downwards
      return false;
    }
    if (getSwitch() && speed < 0) {
      // if the switch is pressed and we're trying to rotate down, don't
      return false;
    }
    if (angle >= GrabberConfig.MAX_ANGLE && speed > 0) {
      // if the angle is our software angle at the top and we are trying to rotate up,
      // don't
      return false;
    }
    return true;
  }

  public void setMotorSpeed(double speed) {
    if (!applySoftwareStops(speed)) {
      stopMotor();
    } else {
      motor.set(speed);
    }
  }

  public void stopMotor() {
    motor.set(0);
  }

  public boolean getSwitch() {
    return !limitSwitch.get();
  }

  public double getAngle() {
    return angle;
  }

  public PIDController getMotorController() {
    return motorController;
  }

  public Command closeFinger() {
    pistonEntry.setString("In");
    return new InstantCommand(() -> piston.set(Value.kForward), this);
  }

  public Command openFinger() {
    pistonEntry.setString("Out");
    return new InstantCommand(() -> piston.set(Value.kReverse), this);
  }

  public Command raiseGrabber() {
    return new InstantCommand(() -> setMotorSpeed(GrabberConfig.GRABBER_MOTOR_SPEED));
  }

  public Command lowerGrabber() {
    return new InstantCommand(() -> setMotorSpeed(-GrabberConfig.GRABBER_MOTOR_SPEED));
  }

  public Command stop() {
    return new InstantCommand(() -> setMotorSpeed(0));
  }
}
