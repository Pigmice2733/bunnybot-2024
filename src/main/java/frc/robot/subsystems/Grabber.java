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
        .withSize(1, 5)
        .withPosition(2, 0);
    motorEntry = grabberEntries.add("Motor Speed", 0).withWidget(BuiltInWidgets.kNumberSlider).getEntry();
    angleEntry = grabberEntries.add("Grabber Angle", 0).getEntry();
    pistonEntry = grabberEntries.add("Piston Extended", false).getEntry();
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
    angle = motor.getEncoder().getPosition() * 360;
    double speed = applySoftwareStops(motor.get());
    motor.set(speed);
    updateEntries();
  }

  public void unzero() {
    zeroed = false;
  }

  public void zero() {
    angle = 0;
    motor.getEncoder().setPosition(0);
    zeroed = true;
  }

  private void updateEntries() {
    motorEntry.setDouble(motor.get());
    switchEntry.setBoolean(getSwitch());
    angleEntry.setDouble(angle);
    zeroedEntry.setBoolean(zeroed);
    pistonEntry.setBoolean(isPistonExtended());
  }

  public double applySoftwareStops(double speed) {
    if (speed > 0) {
      if (!zeroed) {
        return 0;
      }
      if (angle >= GrabberConfig.MAX_ANGLE_PISTON_IN) {
        return 0;
      }
      if (angle >= GrabberConfig.MAX_ANGLE_PISTON_OUT && isPistonExtended()) {
        return 0;
      }
    } else if (speed < 0) {
      if (getSwitch()) {
        return 0;
      }
    }
    return speed;
  }

  public void setMotorSpeed(double speed) {
    speed = applySoftwareStops(speed);
    motor.set(speed);
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

  private boolean isPistonExtended() {
    return piston.get() == Value.kForward;
  }

  public Command closeFinger() {
    return new InstantCommand(() -> piston.set(Value.kForward), this);
  }

  public Command openFinger() {
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
